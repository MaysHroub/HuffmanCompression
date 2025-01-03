package com.msreem.huffmancoding.stack;

import java.lang.reflect.Array;

// Stack implementation using an array.
public class ArrayStack<T extends Comparable<T>> implements Stackable<T> {
	
	private T[] arr;
	private int top;
	
	@SuppressWarnings("unchecked")
	public ArrayStack(Class<T> clazz, int size) {
		arr = (T[]) Array.newInstance(clazz, size);
	}

	@Override
	public void push(T data) {
		if (top != arr.length)
			arr[top++] = data;
	}

	@Override
	public T pop() {
		if (isEmpty()) return null; 
		return arr[--top];
	}

	@Override
	public T peek() {
		if (isEmpty()) return null;
		return arr[top - 1];
	}

	@Override
	public boolean isEmpty() {
		return top == 0;
	}

	@Override
	public void clear() {
		top = 0;
	}
	
}