package com.msreem.huffmancoding.tabledata;

public class HuffmanData {

    private int unsignedByteVal;
    private char asciiValue;
    private int frequency;
    private String code;
    private int codeLength;


    public HuffmanData() {
    }

    public HuffmanData(int unsignedByteVal, int frequency, String code) {
        this.unsignedByteVal = unsignedByteVal;
        this.asciiValue = (char) (unsignedByteVal);
        this.frequency = frequency;
        this.code = code;
        this.codeLength = code.length();
    }

    public int getUnsignedByteVal() {
        return unsignedByteVal;
    }

    public void setUnsignedByteVal(int unsignedByteVal) {
        this.unsignedByteVal = unsignedByteVal;
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
