package br.ufc.crateus.eda.btree.dtypes;

import java.io.RandomAccessFile;

public class DoubleDT extends DataType<Double> {

	@Override
	public Double read(RandomAccessFile fileStore) throws Exception {
		return fileStore.readDouble();
	}

	@Override
	public void write(Double val, RandomAccessFile fileStore) throws Exception {
		fileStore.writeDouble(val);
	}

	@Override
	public Double getSentinel() {
		return Double.MIN_VALUE;
	}

	@Override
	public Double getDefaultValue() {
		return 0.0;
	}
	
	@Override
	public int size() {
		return 8;
	}
}
