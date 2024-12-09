package com.msreem.huffmancoding.heap;

// An interface to encapsulate the methods of MinHeaps
public interface MinHeapInterface<T extends Comparable<T>> {

    void add(T data);
    T removeMin();
    T getMin();
    boolean isEmpty();
    int getSize();
    void clear();

}