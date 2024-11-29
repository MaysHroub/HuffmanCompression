package com.msreem.huffmancoding;

import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {
        testWithTxtFile();
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

    private static void testWithTxtFile() throws IOException {
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
    }

}
