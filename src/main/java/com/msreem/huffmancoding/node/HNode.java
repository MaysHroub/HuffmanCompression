package com.msreem.huffmancoding.node;

public class HNode implements Comparable<HNode> {

    private int freq;
    private byte byteVal;
    private HNode left, right;

    public HNode(int freq) {
        setFreq(freq);
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
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

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
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
