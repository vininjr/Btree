package br.ufc.crateus.eda.btree;

import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchST<K extends Comparable<K>, V> {
	private K[] keys;
	private V[] values;
	private int length;
	
	@SuppressWarnings("unchecked")
	public BinarySearchST(int size) {
		this((K[]) new Comparable[size], (V[]) new Object[size], 0);
	}
	
	public BinarySearchST(K[] keys, V[] values, int length) {
		this.keys = keys;
		this.values = values;
		this.length = length;
	}
	
	@SuppressWarnings("unchecked")
	public BinarySearchST<K, V> split() {
		K[] sKeys = (K[]) new Comparable[keys.length];
		V[] sValues = (V[]) new Object[values.length];
		
		
		for (int i = length / 2; i < length; i++) {
			sKeys[i - length / 2] = keys[i];
			sValues[i - length / 2] = values[i];
		}
		
		int newLength = this.length - this.length / 2;
		BinarySearchST<K, V> newST = new BinarySearchST<>(sKeys, sValues, newLength);
		
		this.length /= 2;
		return newST;
	}
	
	public int rank(K key) {
		int lo = 0, hi = length - 1;
		while (lo <= hi) {
			int m = (lo + hi) / 2;
			int cmp = key.compareTo(keys[m]);
			if (cmp < 0) hi = m - 1;
			else if (cmp > 0) lo = m + 1;
			else return m;
		}
		return lo;
	}

	public void put(K key, V value) {
		int j = rank(key);
 		if (!key.equals(keys[j])) {
 			for (int i = length; i > j; i--) { 
 				keys[i] = keys[i - 1];
 				values[i] = values[i - 1];
 			}
 			length++;
 		}
 		keys[j] = key;
 		values[j] = value;
	}

	public V get(K key) {
		int j = rank(key);
 		return (key.equals(keys[j]))? values[j] : null;
	}

	public void delete(K key) {
		int j = rank(key);
		if (key.equals(keys[j])) {
 			for (int i = j; i < length - 1; i++) {
 				keys[i] = keys[i + 1];
 				values[i] = values[i + 1];
 			}
 			length--;
		}
	}
	
	public boolean contains(K key) {
		return get(key) != null;
	}

	
	public boolean isEmpty() {
		return length == 0;
	}

	public int size() {
		return length;
	}

	public Iterable<K> keys() {
		Queue<K> queue = new LinkedList<>();
		for (int i = 0; i < length; i++) queue.add(keys[i]);
		return queue;
	}
	
	public Iterable<V> values() {
		Queue<V> queue = new LinkedList<>();
		for (int i = 0; i < length; i++) queue.add(values[i]);
		return queue;
	}

	public K min() {
		return keys[0];
	}

	public K max() {
		return keys[length - 1];
	}

	public K floor(K key) {
		int j = rank(key);
		if (key.equals(keys[j])) return key;
		return (j > 0)? keys[j - 1] : null;
	}

	public K ceiling(K key) {
		int j = rank(key);
		return keys[j];
	}

	public K select(int i) {
		return keys[i];
	}

	public Iterable<K> keys(K lo, K hi) {
		return null;
	}

	public int size(K lo, K hi) {
		return rank(hi) - rank(lo);
	}

	public void deleteMax() {
		length--;
	}

	public void deleteMin() {
		for (int i = 0; i < length - 1; i++) {
			keys[i] = keys[i + 1];
			values[i] = values[i + 1];
		}
		length--;
	}
	
	public static void main(String[] args) {
		BinarySearchST<String, Integer> st = new BinarySearchST<>(10);
		st.put("abc", 1);
		st.put("bcd", 2);
		st.put("cade", 3);
		st.put("ab", 3);
		st.put("boca", 3);
		st.put("nada", 3);
		
		for (String key : st.keys()) System.out.println(key);
		System.out.println("Floor: " + st.floor("cadela"));
		System.out.println("Floor: " + st.ceiling("abacate"));
	}
}
