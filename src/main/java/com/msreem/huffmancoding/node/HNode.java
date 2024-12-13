package com.msreem.huffmancoding.node;

// This class represents the nodes of which a huffman coding tree consists.
public class HNode implements Comparable<HNode> {

    private byte byteVal;
    private int freq;
    private HNode left, right;

    public HNode(int freq) {
        setFreq(freq);
    }

    public HNode(int freq, byte byteVal) {
        setFreq(freq);
        setByteVal(byteVal);
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public byte getByteVal() {
        return byteVal;
    }

    public void setByteVal(byte byteVal) {
        this.byteVal = byteVal;
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

}
