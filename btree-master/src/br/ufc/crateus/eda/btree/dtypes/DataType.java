package br.ufc.crateus.eda.btree.dtypes;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public abstract class DataType<T> {
	
	public List<T> read(RandomAccessFile fileStore, int m) throws Exception {
		List<T> list = new ArrayList<>(m);
		for (int i = 0; i < m; i++) list.add(read(fileStore));
		return list;
	}
	
	public abstract T read(RandomAccessFile fileStore) throws Exception;
	
	public abstract void write(T val, RandomAccessFile fileStore) throws Exception;
	
	public void write(RandomAccessFile fileStore, Iterable<T> values, int m) throws Exception {
		int count = 0;
		for (T val : values) {
			write(val, fileStore);
			count++;
		}
		for (int i = 0; i < m - count; i++) write(getDefaultValue(), fileStore);
	}
	
	public abstract T getSentinel();
	
	public abstract T getDefaultValue();
	
	public abstract int size();
}
