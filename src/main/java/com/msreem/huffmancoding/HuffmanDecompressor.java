package com.msreem.huffmancoding;

import com.msreem.huffmancoding.node.HNode;

import java.io.*;
import java.util.Arrays;
import java.util.Stack;

public class HuffmanDecompressor {

    public static final int BUFFER_SIZE = 8, BYTE_RANGE = 256;

    private String compressedFileName;
    private String originalFileExtension;
    private int headerSizeInBits;
    private HNode root;
    private DataInputStream din;

    public HuffmanDecompressor(String compressedFileName) throws IOException {
        if (compressedFileName == null || compressedFileName.isEmpty())
            throw new IllegalArgumentException("Invalid file name.");

        this.compressedFileName = compressedFileName;
        if (!getFileExtension().equalsIgnoreCase("huf"))
            throw new IllegalArgumentException("Invalid file extension.");
    }

    public void decompress() throws IOException {
        din = new DataInputStream(new FileInputStream(compressedFileName));
        readHeader();
        reconstructHuffmanCodingTree();
        // initHuffmanCodesArray();
        decodeDataToOriginalFile();
    }

    private void decodeDataToOriginalFile() throws IOException {
        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(getOriginalFileName()))) {

            byte numOfBytesRead = 0;
            byte[] bufferIn = new byte[BUFFER_SIZE], bufferOut = new byte[BUFFER_SIZE];
            int outIdx = 0;
            HNode curr = root;

            while ((numOfBytesRead = (byte) din.read(bufferIn)) != -1)
                for (int i = 0; i < numOfBytesRead; i++)
                    for (int j = 0; j < 8; j++) {
                        int bit = (bufferIn[i] >>> (7-j)) & 1;

                        if (bit == 0) curr = curr.getLeft();
                        else curr = curr.getRight();

                        if (curr.isLeaf()) {
                            bufferOut[outIdx++] = curr.getByteVal();
                            curr = root;
                        }

                        if (outIdx == BUFFER_SIZE) {
                            dout.write(bufferOut);
                            Arrays.fill(bufferOut, (byte) 0);
                            outIdx = 0;
                        }
                    }

            for (byte b : bufferOut)
                if (b != 0)
                    dout.write(b);
        }
    }

    private void readHeader() throws IOException {
        originalFileExtension = din.readUTF();
        headerSizeInBits = din.readInt();
    }

    private void reconstructHuffmanCodingTree() throws IOException {
        Stack<HNode> stack = new Stack<>();
        byte[] buffer = readTreeStructure(headerSizeInBits);
        int bitIdx = 0, byteIdx = 0;
        for (int i = 0; i < headerSizeInBits; i++) {
            bitIdx = i % 8;
            byteIdx = i / 8;
            int bit = (buffer[byteIdx] >>> (7-bitIdx)) & 1;
            if (bit == 1) {
                byte byteVal = 0;
                for (int j = 0; j < 8; j++) {
                    i++;
                    bitIdx = i % 8;
                    byteIdx = i / 8;
                    bit = (buffer[byteIdx] >>> (7-bitIdx)) & 1;
                    byteVal |= (byte) (bit << (7-j));
                }
                HNode node = new HNode(0, byteVal);
                stack.push(node);
            } else {
                HNode rightNode = stack.pop(), leftNode = stack.pop();
                HNode node = new HNode(0);
                node.setLeft(leftNode);
                node.setRight(rightNode);
                stack.push(node);
            }
        }
        root = stack.pop();
    }

    private byte[] readTreeStructure(int headerSize) throws IOException {
        int bufferSize = (int) Math.ceil(headerSize / 8.0);
        byte[] buffer = new byte[bufferSize];
        din.read(buffer);
        return buffer;
    }

//    private void initHuffmanCodesArray() {
//        huffmanCodes = new String[BYTE_RANGE];
//        generateHuffmanCodes(root, "", huffmanCodes);
//    }

//    private void generateHuffmanCodes(HNode node, String code, String[] huffmanCodes) {
//        if (node.isLeaf()) {
//            huffmanCodes[node.getByteVal()+128] = code;
//            return;
//        }
//        generateHuffmanCodes(node.getLeft(), code + "0", huffmanCodes);
//        generateHuffmanCodes(node.getRight(), code + "1", huffmanCodes);
//    }

    private String getFileExtension() {
        int index = compressedFileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return compressedFileName.substring(index+1);
    }

    private String getOriginalFileName() {
        int index = compressedFileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return compressedFileName.substring(0, index+1) + originalFileExtension;
    }

}
