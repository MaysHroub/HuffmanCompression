package com.msreem.huffmancoding;

import com.msreem.huffmancoding.heap.MinHeap;
import com.msreem.huffmancoding.node.HNode;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class HuffmanCompressor {

    public static final int BUFFER_SIZE = 8, BYTE_RANGE = 256;

    private String originalFileName;
    private HNode root;
    private String[] huffmanCodes;
    private DataOutputStream dout;


    public HuffmanCompressor(String originalFileName) throws IOException {
        if (originalFileName == null || originalFileName.isEmpty())
            throw new IllegalArgumentException("Invalid file name.");

        this.originalFileName = originalFileName;
        if (getFileExtension().equalsIgnoreCase("huf"))
            throw new IllegalArgumentException("Invalid file extension.");
    }

    public void compress() throws IOException {
        root = buildHuffmanTree(countFrequencies());
        initHuffmanCodes(root);
        dout = new DataOutputStream(new FileOutputStream(getCompressedFileName(), true));

        writeHeader();
        writeData();
        dout.close();
    }

    private void writeData() throws IOException {
        try (DataInputStream din = new DataInputStream(new FileInputStream(originalFileName))) {
            byte numOfBytesRead = 0;
            byte[] bufferIn = new byte[BUFFER_SIZE], bufferOut = new byte[BUFFER_SIZE];
            int bitIdx, byteIdx, itr = 0;
            while ((numOfBytesRead = (byte) din.read(bufferIn)) != -1)
                for (int i = 0; i < numOfBytesRead; i++) {
                    String code = huffmanCodes[bufferIn[i]];
                    for (int j = 0; j < code.length(); j++) {
                        if (itr == BUFFER_SIZE * 8) {
                            dout.write(bufferOut);
                            itr = 0;
                            Arrays.fill(bufferOut, (byte) 0);
                        }
                        int bit = code.charAt(j) - '0';
                        bitIdx = itr % 8;
                        byteIdx = itr / 8;
                        bufferOut[byteIdx] |= (byte) (bit << (7-bitIdx));
                        itr++;
                    }
                }

            for (byte b : bufferOut)
                if (b != 0)
                    dout.write(b);
        }
    }

    private void writeHeader() throws IOException {
        dout.writeUTF(getFileExtension());
        dout.writeInt(getFileSizeInBytes()); // or write number of padding bits
        int headerSizeInBits = getHeaderSizeInBits(root);
        dout.writeInt(headerSizeInBits);
        byte[] buffer = new byte[headerSizeInBits];
        writeHuffmanTreeStructure(root, buffer, 0, headerSizeInBits);
    }

    // pre-order traversal
    private void writeHuffmanTreeStructure(HNode node, byte[] buffer, int itr, int n) throws IOException {
        if (node == null) return;
        if (itr == n-1) dout.write(buffer);
        if (node.isLeaf()) {
            int bitIdx = itr%8, byteIdx = itr/8;
            buffer[byteIdx] |= (byte) (1 << (7-bitIdx));
            itr++;

            byte byteVal = node.getByteVal();
            for (int i = 0; i < 8; i++) {
                int bit = byteVal & (1 << (7-i));
                bitIdx = itr%8; byteIdx = itr/8;
                buffer[byteIdx] |= (byte) (bit << (7-bitIdx));
                itr++;
            }
            return;
        }
        writeHuffmanTreeStructure(node.getLeft(), buffer, itr, n);
        writeHuffmanTreeStructure(node.getRight(), buffer, itr, n);
    }

    private int getFileSizeInBytes() {
        return root.getFreq();
    }

    private String getFileExtension() {
        int index = originalFileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return originalFileName.substring(index+1);
    }

    private String getCompressedFileName() {
        int index = originalFileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return originalFileName.substring(0, index+1) + "huf";
    }

    private int getHeaderSizeInBits(HNode root) {
        int internals = 0, leaves = 0;
        Queue<HNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            HNode curr = queue.poll();
            if (curr.isLeaf()) leaves++;
            else internals++;
            if (curr.getLeft() != null)
                queue.offer(curr.getLeft());
            if (curr.getRight() != null)
                queue.offer(curr.getRight());
        }
        return internals + 9*leaves;
    }

    private void initHuffmanCodes(HNode root) {
        huffmanCodes = new String[BYTE_RANGE];
        generateHuffmanCodes(root, "", huffmanCodes);
    }

    private void generateHuffmanCodes(HNode node, String code, String[] huffmanCodes) {
        if (node.isLeaf()) {
            huffmanCodes[node.getByteVal() + 128] = code;
            return;
        }
        generateHuffmanCodes(node.getLeft(), code + "0", huffmanCodes);
        generateHuffmanCodes(node.getRight(), code + "1", huffmanCodes);
    }

    private HNode buildHuffmanTree(int[] freq) {
        MinHeap<HNode> minHeap = getMinHeap(freq);
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
        return minHeap.getMin();
    }

    private MinHeap<HNode> getMinHeap(int[] freq) {
        MinHeap<HNode> minHeap = new MinHeap<>(BYTE_RANGE);
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] == 0) continue;
            minHeap.add(new HNode(freq[i], (byte) i));
        }
        return minHeap;
    }

    private int[] countFrequencies() throws IOException {
        // to store frequency of each byte (00000000 to 11111111) -> 256 possibility
        int[] freq = new int[256];

        // buffer to hold 8 bytes
        byte[] buffer = new byte[BUFFER_SIZE];

        byte numOfBytesRead = 0;
        try (DataInputStream din = new DataInputStream(new FileInputStream(originalFileName))) {
            while ((numOfBytesRead = (byte) din.read(buffer)) != -1)
                for (int i = 0; i < numOfBytesRead; i++)
                    freq[buffer[i] + 128]++;   // byte value range: (-128, 127) -> so we add 128 to index
        }
        return freq;
    }

}

















