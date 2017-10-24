package br.ufc.crateus.eda.btree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import br.ufc.crateus.eda.btree.dtypes.DataType;
import br.ufc.crateus.eda.btree.dtypes.LongDT;

public class PageSerializer<K extends Comparable<K>> {
	private DataType<K> keyDT;
	private LongDT adressDT;
	private int m;
	private File file; 
	
	static final int FILE_SIZE_OFFSET = 0;
	static final int N_KEYS_OFFSET = 8;
	static final int ROOT_OFFSET = 12;
	
	private long fileSize;
	private int nKeys;
	
	PageSerializer(File file, DataType<K> dt, int m) throws IOException {
		this.file = file;
		this.keyDT = dt;
		this.adressDT = new LongDT();
		this.m = m;
		
		if (file.exists()) {
			RandomAccessFile fileStore = new RandomAccessFile(file, "r"); 
			this.fileSize = fileStore.readLong();
			this.nKeys = fileStore.readInt();
			fileStore.close();
		}
		else {
			RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
			fileStore.writeLong(12L);
			fileStore.writeInt(0);
			this.fileSize = ROOT_OFFSET;
			this.nKeys = 0;
			fileStore.close();
		}
	}
	
	private int pageSize() {
		return 1 + 4 + m * (keyDT.size() + adressDT.size());  
	}
	
	private void write(long offset, Page<K> page, boolean append) throws Exception {
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		fileStore.seek(offset);
		fileStore.writeBoolean(page.isExternal());
		BinarySearchST<K, Long> st = page.asSymbolTable();
		fileStore.writeInt(st.size());
		keyDT.write(fileStore, st.keys(), m);
		adressDT.write(fileStore, st.values(), m);
		
		if (append) {
			fileSize += pageSize();
			fileStore.seek(FILE_SIZE_OFFSET);
			fileStore.writeLong(fileSize);
			fileStore.close();
		}
		
		fileStore.close();
	}
	
	void write(long offset, Page<K> page) throws Exception {
		write(offset, page, false);
	}
	
	Page<K> append(BinarySearchST<K, Long> st, boolean bottom) throws Exception {
		Page<K> page = new Page<>(st, bottom, fileSize, this);
		write(fileSize, page, true);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	Page<K> read(long offset) throws Exception {
		RandomAccessFile fileStore = new RandomAccessFile(file, "r");
		fileStore.seek(offset);
		
		boolean botton = fileStore.readBoolean();
		int size = fileStore.readInt();
		
		List<K> lKeys = keyDT.read(fileStore, m);
		K[] keys = (K[]) lKeys.toArray(new Comparable[m]);
		Long[] values = adressDT.read(fileStore, m).toArray(new Long[m]);
		
		BinarySearchST<K, Long> st = new BinarySearchST<>(keys, values, size);
		fileStore.close();
		return new Page<>(st, botton, offset, this);
	}
	
	int getPageSize() {
		return m;
	}

	Page<K> readRoot() throws Exception {
		Page<K> root = null;
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		if (fileSize > ROOT_OFFSET) root = read(ROOT_OFFSET);
		else {
			BinarySearchST<K, Long> st = new BinarySearchST<>(m);
			st.put(keyDT.getSentinel(), -1L);
			root = append(st, true);
		}
		fileStore.close();
		return root;
	}
	
	void keyInserted() throws IOException {
		nKeys++;
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		fileStore.seek(N_KEYS_OFFSET);
		fileStore.write(nKeys);
		fileStore.close();
	}
	
	public int getNumberOfKeys() {
		return nKeys;
	}
}
