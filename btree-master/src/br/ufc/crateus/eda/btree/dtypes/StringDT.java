package br.ufc.crateus.eda.btree.dtypes;

import java.io.RandomAccessFile;

public class StringDT extends DataType<String> {
	
	private int size;
	
	public StringDT(int size) {
		this.size = size;
	}

	private static String fixedLengthString(String str, int size) {
		if (str.length() > size) return str.substring(0, size);
		else return String.format("%1$" + size + "s", str);
	}

	@Override
	public String read(RandomAccessFile fileStore) throws Exception {
		byte[] bytes = new byte[size];
		fileStore.read(bytes);
		return new String(bytes, "ISO-8859-1").trim();
	}

	@Override
	public void write(String val, RandomAccessFile fileStore) throws Exception {
		fileStore.write(fixedLengthString(val, size).getBytes("ISO-8859-1"));
	}
	
	@Override
	public String getSentinel() {
		return "";
	}

	@Override
	public String getDefaultValue() {
		return "";
	}
	
	@Override
	public int size() {
		return size;
	}
}
