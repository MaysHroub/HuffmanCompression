package com.msreem.huffmancoding;

import com.msreem.huffmancoding.node.HNode;
import com.msreem.huffmancoding.stack.ArrayStack;

import java.io.*;
import java.util.Arrays;

public class HuffmanDecompressor {

    public static final int BUFFER_SIZE = 8;

    private File compressedFile, decompressedFile;
    private String originalFileExtension;
    private byte numOfPaddingBits;
    private int headerSizeInBits;
    private HNode root;
    private DataInputStream din;


    public HuffmanDecompressor() {

    }

    public HuffmanDecompressor(File compressedFile) throws FileNotFoundException {
        setCompressedFile(compressedFile);
    }


    public void setCompressedFile(File compressedFile) throws IllegalArgumentException, FileNotFoundException {
        if (compressedFile == null || !compressedFile.exists())
            throw new IllegalArgumentException("Invalid file.");

        this.compressedFile = compressedFile;
        if (!getCompressedFileExtension().equalsIgnoreCase("huf"))
            throw new IllegalArgumentException("Invalid file extension.");

        din = new DataInputStream(new FileInputStream(compressedFile));
    }

    public File getCompressedFile() {
        return compressedFile;
    }

    public File getDecompressedFile() {
        return decompressedFile;
    }


    public void decompress() throws IOException {
        readHeader();
        reconstructHuffmanCodingTree();

        initDecompressedFile();
        decodeDataToOriginalFile();
    }

    private void readHeader() throws IOException {
        originalFileExtension = din.readUTF();
        numOfPaddingBits = din.readByte();
        headerSizeInBits = din.readInt();
    }

    private void reconstructHuffmanCodingTree() throws IOException {
        ArrayStack<HNode> stack = new ArrayStack<>(HNode.class, headerSizeInBits);
        byte[] buffer = readTreeStructure();
        int bitIdx = 0, byteIdx = 0;
        for (int i = 0; i < headerSizeInBits; i++) {
            bitIdx = i % 8;
            byteIdx = i / 8;
            int bit = (buffer[byteIdx] >>> (7 - bitIdx)) & 1;
            if (bit == 1) {
                byte byteVal = 0;
                for (int j = 0; j < 8; j++) {
                    i++;
                    bitIdx = i % 8;
                    byteIdx = i / 8;
                    bit = (buffer[byteIdx] >>> (7 - bitIdx)) & 1;
                    byteVal |= (byte) (bit << (7 - j));
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

    private void decodeDataToOriginalFile() throws IOException {
        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(decompressedFile))) {

            byte numOfBytesRead = 0;
            byte[] bufferIn = new byte[BUFFER_SIZE], bufferOut = new byte[BUFFER_SIZE];
            int outIdx = 0, numOfBitsToRead = 8;
            boolean ignorePaddingBits = false;
            HNode curr = root;

            while ((numOfBytesRead = (byte) din.read(bufferIn)) != -1) {
                if (numOfBytesRead < 8 || din.available() == -1)
                    ignorePaddingBits = true;

                for (int i = 0; i < numOfBytesRead; i++) {
                    if (ignorePaddingBits && i == numOfBytesRead - 1)
                        numOfBitsToRead = 8 - numOfPaddingBits;

                    for (int j = 0; j < numOfBitsToRead; j++) {
                        int bit = (bufferIn[i] >>> (7 - j)) & 1;

                        if (bit == 0) curr = curr.getLeft();
                        else curr = curr.getRight();

                        if (curr.isLeaf()) {
                            bufferOut[outIdx++] = (byte) curr.getUnsignedByteVal();
                            curr = root;
                        }

                        if (outIdx == BUFFER_SIZE) {
                            dout.write(bufferOut);
                            Arrays.fill(bufferOut, (byte) 0);
                            outIdx = 0;
                        }
                    }
                }
            }
            for (byte b : bufferOut)
                if (b != 0)
                    dout.write(b);
        }
    }

    private byte[] readTreeStructure() throws IOException {
        int bufferSize = (int) Math.ceil(headerSizeInBits / 8.0);
        byte[] buffer = new byte[bufferSize];
        din.read(buffer);
        return buffer;
    }

    private String getCompressedFileExtension() {
        String[] tokens = compressedFile.getName().split("\\.");
        if (tokens.length < 2)
            return "";
        return tokens[1];
    }

    private void initDecompressedFile() {
        int index = compressedFile.getAbsolutePath().lastIndexOf(".");
        if (index != -1)
            decompressedFile = new File(compressedFile.getAbsolutePath().substring(0, index) + "_." + originalFileExtension);
    }

}
