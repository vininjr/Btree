package br.ufc.crateus.eda.btree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import br.ufc.crateus.eda.btree.dtypes.DataType;

public class DataSerializer<V> {
	private DataType<V> valueDT;
	private File file;
	
	static final int FILE_SIZE_OFFSET = 0;
	private long fileSize;
	
	DataSerializer(File file, DataType<V> dt) throws IOException {
		this.file = file;
		this.valueDT = dt;
		fileSize = getFileSize();
	}
	
	V read(long offset) throws Exception {
		RandomAccessFile fileStore = new RandomAccessFile(file, "r"); 
		fileStore.seek(offset);
		V value = valueDT.read(fileStore);
		fileStore.close();
		return value;
	}
	
	private void write(long offset, V val, boolean append) throws Exception {
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw"); 
		fileStore.seek(offset);
		valueDT.write(val, fileStore);
		
		if (append) {
			fileSize += valueDT.size();
			fileStore.seek(FILE_SIZE_OFFSET);
			fileStore.writeLong(fileSize);
		}
		
		fileStore.close();
	}
	
	void write(long offset, V val) throws Exception {
		write(offset, val, false);
	}
	
	long append(V val) throws Exception { 
		long lastSize = fileSize;
		write(fileSize, val, true);
		return lastSize;
	}
	
	private long getFileSize() throws IOException {
		if (file.exists()) {
			RandomAccessFile fileStore = new RandomAccessFile(file, "r"); 
			fileStore.seek(FILE_SIZE_OFFSET);
			long fileSize = fileStore.readLong();
			fileStore.close();
			return fileSize;
		}
		else {
			RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
			fileStore.seek(FILE_SIZE_OFFSET);
			fileStore.writeLong(8L);
			fileStore.close();
			return 8L;
		}
	}
}
