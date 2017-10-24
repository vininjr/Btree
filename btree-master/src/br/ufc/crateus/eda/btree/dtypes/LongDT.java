package br.ufc.crateus.eda.btree.dtypes;

import java.io.RandomAccessFile;

public class LongDT extends DataType<Long> {

	@Override
	public Long read(RandomAccessFile fileStore) throws Exception {
		return fileStore.readLong();
	}

	@Override
	public void write(Long val, RandomAccessFile fileStore) throws Exception {
		fileStore.writeLong(val);
	}

	@Override
	public Long getSentinel() {
		return Long.MIN_VALUE;
	}

	@Override
	public Long getDefaultValue() {
		return 0L;
	}
	
	@Override
	public int size() {
		return 8;
	}

}
