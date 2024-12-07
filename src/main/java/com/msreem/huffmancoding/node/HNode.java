package com.msreem.huffmancoding.node;

public class HNode implements Comparable<HNode> {

    private int unsignedByteVal;
    private byte bitCode;
    private int freq;
    private HNode left, right;

    public HNode(int freq) {
        setFreq(freq);
    }

    public HNode(int freq, int unsignedByteVal) {
        setFreq(freq);
        setUnsignedByteVal(unsignedByteVal);
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getUnsignedByteVal() {
        return unsignedByteVal;
    }

    public void setUnsignedByteVal(int unsignedByteVal) {
        this.unsignedByteVal = unsignedByteVal;
    }

    public HNode getLeft() {
        return left;
    }

    public void setLeft(HNode left) {
        this.left = left;
    }

    public HNode getRight() {
        return right;
    }

    public void setRight(HNode right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public String toString() {
        return "[" + freq + "]";
    }

    @Override
    public int compareTo(HNode o) {
        return freq - o.getFreq();
    }

    public byte getBitCode() {
        return bitCode;
    }

    public void setBitCode(byte bitCode) {
        this.bitCode = bitCode;
    }
}
