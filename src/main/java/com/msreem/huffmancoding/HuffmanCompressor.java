package com.msreem.huffmancoding;

import com.msreem.huffmancoding.heap.MinHeap;
import com.msreem.huffmancoding.node.HNode;

import java.io.*;
import java.util.Arrays;

public class HuffmanCompressor {

    public static final int BUFFER_SIZE = 8, BYTE_RANGE = 256, INTERNAL_NODE_IDX = 0, LEAF_NODE_IDX = 1;

    private String originalFileName;
    private HNode root;
    private int[] frequencies;
    private String[] huffmanCodes;
    private DataOutputStream dout;


    public HuffmanCompressor(String originalFileName) {
        if (originalFileName == null || originalFileName.isEmpty())
            throw new IllegalArgumentException("Invalid file name.");

        this.originalFileName = originalFileName;
        if (getFileExtension().equalsIgnoreCase("huf"))
            throw new IllegalArgumentException("Invalid file extension.");
    }


    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public int[] getFrequencies() {
        return frequencies;
    }

    public String[] getHuffmanCodes() {
        return huffmanCodes;
    }

    public void compress() throws IOException {
        countFrequencies();
        buildHuffmanTree();
        initHuffmanCodesArray();

        dout = new DataOutputStream(new FileOutputStream(getCompressedFileName()));
        writeHeader();
        writeData();
        dout.close();
    }

    private void countFrequencies() throws IOException {
        // to store frequency of each byte (00000000 to 11111111) -> 256 possibility
        frequencies = new int[BYTE_RANGE];

        // buffer to hold 8 bytes
        byte[] buffer = new byte[BUFFER_SIZE];

        byte numOfBytesRead = 0;
        try (DataInputStream din = new DataInputStream(new FileInputStream(originalFileName))) {
            while ((numOfBytesRead = (byte) din.read(buffer)) != -1)
                for (int i = 0; i < numOfBytesRead; i++)
                    frequencies[buffer[i] + 128]++;   // byte value range: (-128, 127) -> so we add 128 to index
        }
    }

    private void buildHuffmanTree() {
        MinHeap<HNode> minHeap = getMinHeap(frequencies);
        int numOfBytes = minHeap.getSize();

        for (int i = 0; i < numOfBytes - 1; i++) {
            HNode x = minHeap.removeMin(), y = minHeap.removeMin();
            HNode z = new HNode(x.getFreq() + y.getFreq());
            x.setBitCode((byte) 0);
            y.setBitCode((byte) 1);
            z.setLeft(x);
            z.setRight(y);
            minHeap.add(z);
        }
        root = minHeap.getMin();
    }

    private void initHuffmanCodesArray() {
        huffmanCodes = new String[BYTE_RANGE];
        generateHuffmanCodes(root, "", huffmanCodes);
    }

    private void generateHuffmanCodes(HNode node, String code, String[] huffmanCodes) {
        if (node.isLeaf()) {
            huffmanCodes[node.getUnsignedByteVal()] = code;
            return;
        }
        generateHuffmanCodes(node.getLeft(), code + "0", huffmanCodes);
        generateHuffmanCodes(node.getRight(), code + "1", huffmanCodes);
    }

    private void writeHeader() throws IOException {
        dout.writeUTF(getFileExtension());
        dout.writeByte(getNumberOfPaddingBits());
        int headerSizeInBits = getHeaderSizeInBits(root);
        dout.writeInt(headerSizeInBits);
        byte[] buffer = new byte[(int) Math.ceil(headerSizeInBits / 8.0)];
        writeHuffmanTreeStructure(root, buffer, 0, headerSizeInBits);
    }

    private void writeData() throws IOException {
        try (DataInputStream din = new DataInputStream(new FileInputStream(originalFileName))) {
            byte numOfBytesRead = 0;
            byte[] bufferIn = new byte[BUFFER_SIZE], bufferOut = new byte[BUFFER_SIZE];
            int bitIdx, byteIdx, itr = 0;
            while ((numOfBytesRead = (byte) din.read(bufferIn)) != -1)

                for (int i = 0; i < numOfBytesRead; i++) {

                    String code = huffmanCodes[bufferIn[i] + 128];

                    for (int j = 0; j < code.length(); j++) {
                        if (itr == BUFFER_SIZE * 8) {
                            dout.write(bufferOut);
                            itr = 0;
                            Arrays.fill(bufferOut, (byte) 0);
                        }
                        int bit = code.charAt(j) - '0';
                        bitIdx = itr % 8;
                        byteIdx = itr / 8;
                        bufferOut[byteIdx] |= (byte) (bit << (7 - bitIdx));
                        itr++;
                    }
                }

            for (byte b : bufferOut)
                if (b != 0)
                    dout.write(b);
        }
    }

    // post-order traversal
    private int writeHuffmanTreeStructure(HNode node, byte[] buffer, int itr, int n) throws IOException {
        if (node == null) return itr;

        itr = writeHuffmanTreeStructure(node.getLeft(), buffer, itr, n);
        itr = writeHuffmanTreeStructure(node.getRight(), buffer, itr, n);

        if (node.isLeaf()) {
            int bitIdx = itr % 8, byteIdx = itr / 8;
            buffer[byteIdx] |= (byte) (1 << (7 - bitIdx));
            itr++;

            int byteVal = node.getUnsignedByteVal() + 128;
            for (int i = 0; i < 8; i++) {
                int bit = (byteVal >>> (7 - i)) & 1;
                bitIdx = itr % 8;
                byteIdx = itr / 8;
                buffer[byteIdx] |= (byte) (bit << (7 - bitIdx));
                itr++;
            }
        } else
            itr++;

        if (itr == n - 1)
            dout.write(buffer);

        return itr;
    }

    private MinHeap<HNode> getMinHeap(int[] freq) {
        MinHeap<HNode> minHeap = new MinHeap<>(BYTE_RANGE);
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] == 0) continue;
            minHeap.add(new HNode(freq[i], i));
        }
        return minHeap;
    }

    private int getNumberOfPaddingBits() {
        int sum = 0;
        for (int i = 0; i < BYTE_RANGE; i++)
            if (huffmanCodes[i] != null)
                sum += frequencies[i] * huffmanCodes[i].length();

        return sum % 8;
    }

    private int getHeaderSizeInBits(HNode root) {
        int[] count = new int[2];
        inOrderCount(root, count);
        return count[INTERNAL_NODE_IDX] + 9 * count[LEAF_NODE_IDX];
    }

    private void inOrderCount(HNode node, int[] count) {
        if (node == null) return;
        inOrderCount(node.getLeft(), count);
        if (node.isLeaf()) count[LEAF_NODE_IDX]++;
        else count[INTERNAL_NODE_IDX]++;
        inOrderCount(node.getRight(), count);
    }

    private String getFileExtension() {
        int index = originalFileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return originalFileName.substring(index + 1);
    }

    private String getCompressedFileName() {
        int index = originalFileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return originalFileName.substring(0, index + 1) + "huf";
    }

}


















