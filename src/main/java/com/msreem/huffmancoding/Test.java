package com.msreem.huffmancoding;

import com.msreem.huffmancoding.heap.MinHeap;
import com.msreem.huffmancoding.node.HNode;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Test {

    public static void main(String[] args) throws IOException {
        String fileName = "src/main/resources/com/msreem/huffmancoding/RandomCharacters.txt";
        HuffmanCompressor huffmanCompressor = new HuffmanCompressor(fileName);
        huffmanCompressor.compress();

        String compressedFileName = "src/main/resources/com/msreem/huffmancoding/RandomCharacters.huf";
        HuffmanDecompressor huffmanDecompressor = new HuffmanDecompressor(compressedFileName);
        huffmanDecompressor.decompress();
    }

}
