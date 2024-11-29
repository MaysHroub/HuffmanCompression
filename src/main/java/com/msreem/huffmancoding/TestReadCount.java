package com.msreem.huffmancoding;

import java.io.*;
import java.util.Scanner;

public class TestReadCount {

    public static void main(String[] args) throws IOException {
        final String filePath = "src/main/resources/com/msreem/huffmancoding/RandomCharacters.txt";
        final byte bufferSize = 8;

        // to store frequency of each byte (00000000 to 11111111) -> 256 possibility
        int[] freq = new int[256];

        // buffer to hold 8 bytes
        byte[] buffer = new byte[bufferSize];

        byte numOfBytesRead = 0;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath), bufferSize)) {
            while ((numOfBytesRead = (byte) bin.read(buffer)) != -1) {
                for (byte b : buffer)
                    freq[b]++;
            }
        }

        for (int i = 0; i < freq.length; i++)
            if (freq[i] != 0)
                System.out.println((char) i + " : " + freq[i]);

    }

}
