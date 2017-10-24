package br.ufc.crateus.eda.btree.dtypes;

import java.io.IOException;
import java.io.RandomAccessFile;

public class IntegerDT extends DataType<Integer> {

	@Override
	public Integer read(RandomAccessFile fileStore) throws IOException {
		return fileStore.readInt();
	}

	@Override
	public void write(Integer val, RandomAccessFile fileStore) throws IOException {
		fileStore.writeInt(val);
	}

	@Override
	public Integer getSentinel() {
		return Integer.MIN_VALUE;
	}

	@Override
	public Integer getDefaultValue() {
		return 1;
	}
	
	@Override
	public int size() {
		return 4;
	}
}
