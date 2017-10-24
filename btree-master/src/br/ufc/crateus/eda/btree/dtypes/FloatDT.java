package br.ufc.crateus.eda.btree.dtypes;

import java.io.RandomAccessFile;

public class FloatDT extends DataType<Float> {

	@Override
	public Float read(RandomAccessFile fileStore) throws Exception {
		return fileStore.readFloat();
	}

	@Override
	public void write(Float val, RandomAccessFile fileStore) throws Exception {
		fileStore.writeFloat(val);
	}

	@Override
	public Float getSentinel() {
		return Float.MIN_VALUE;
	}

	@Override
	public Float getDefaultValue() {
		return 0.0F;
	}
	
	@Override
	public int size() {
		return 4;
	}
}
