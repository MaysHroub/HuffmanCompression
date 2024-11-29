package com.msreem.huffmancoding;

import com.msreem.huffmancoding.heap.MinHeap;
import com.msreem.huffmancoding.node.HNode;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Test {

    public static void main(String[] args) throws IOException {
        int[] freq = testWithTxtFile();

        MinHeap<HNode> minHeap = new MinHeap<>(256);

        int n = 0;
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] == 0) continue;
            minHeap.add(new HNode(freq[i], (byte) i));
            n++;
        }

        for (int i = 0; i < n-1; i++) {
            HNode x = minHeap.removeMin(), y = minHeap.removeMin();
            HNode z = new HNode(x.getFreq() + y.getFreq());
            z.setLeft(x);
            z.setRight(y);
            minHeap.add(z);
        }

        // level-order traversal
        HNode root = minHeap.getMin();
        Queue<HNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            HNode curr = queue.poll();
            System.out.print(curr + " ");
            if (curr.getLeft() != null)
                queue.offer(curr.getLeft());
            if (curr.getRight() != null)
                queue.offer(curr.getRight());
        }
    }

    private static void testWithImg() throws IOException {
        final String filePath = "src/main/resources/com/msreem/huffmancoding/TestImg.jpg";
        final byte bufferSize = 8;

        // to store frequency of each byte (00000000 to 11111111) -> 256 possibility
        int[] freq = new int[256];

        // buffer to hold 8 bytes
        byte[] buffer = new byte[bufferSize];

        byte numOfBytesRead = 0;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath), bufferSize)) {
            while ((numOfBytesRead = (byte) bin.read(buffer)) != -1)
                for (byte b : buffer)
                        freq[b+128]++;   // byte value range: (-128, 127)
        }

        for (int i = 0; i < freq.length; i++)
            if (freq[i] != 0)
                System.out.println(i + " : " + freq[i]);
    }

    private static int[] testWithTxtFile() throws IOException {
        final String filePath = "src/main/resources/com/msreem/huffmancoding/RandomCharacters.txt";
        final byte bufferSize = 8;

        // to store frequency of each byte (00000000 to 11111111) -> 256 possibility
        int[] freq = new int[256];

        // buffer to hold 8 bytes
        byte[] buffer = new byte[bufferSize];

        byte numOfBytesRead = 0;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath), bufferSize)) {
            while ((numOfBytesRead = (byte) bin.read(buffer)) != -1)
                for (byte b : buffer)
                    freq[b+128]++;   // byte value range: (-128, 127)
        }

        for (int i = 0; i < freq.length; i++)
            if (freq[i] != 0)
                System.out.println((char) (i-128) + " : " + freq[i]);

        return freq;
    }

}
