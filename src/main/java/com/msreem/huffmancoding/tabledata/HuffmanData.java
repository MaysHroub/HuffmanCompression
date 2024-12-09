package com.msreem.huffmancoding.tabledata;

// This class encapsulates details about a symbol in Huffman encoding process.
public class HuffmanData {

    private byte byteVal;
    private char asciiValue;
    private int frequency;
    private String code;
    private int codeLength;


    public HuffmanData() {
    }

    public HuffmanData(byte byteVal, int frequency, String code) {
        this.byteVal = byteVal;
        this.asciiValue = (char) (byteVal);
        this.frequency = frequency;
        this.code = code;
        this.codeLength = code.length();
    }

    public byte getByteVal() {
        return byteVal;
    }

    public void setByteVal(byte byteVal) {
        this.byteVal = byteVal;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public char getAsciiValue() {
        return asciiValue;
    }

    public void setAsciiValue(char asciiValue) {
        this.asciiValue = asciiValue;
    }
}
