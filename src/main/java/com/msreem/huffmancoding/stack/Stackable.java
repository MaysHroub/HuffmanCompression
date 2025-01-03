package com.msreem.huffmancoding.stack;

// An interface to encapsulate the methods of Stacks.
public interface Stackable<T extends Comparable<T>> {
	
	void push(T data);
	T pop();
	T peek();
	boolean isEmpty();
	void clear();
	
}