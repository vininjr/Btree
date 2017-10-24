package br.ufc.crateus.eda.btree;


public class Page<K extends Comparable<K>> {
	private boolean bottom;
	
	private BinarySearchST<K, Long> st;
	private PageSerializer<K> serializer;
	private long offset;
	
	public Page(boolean bottom, long offset, PageSerializer<K> serializer) {
		this(new BinarySearchST<>(serializer.getPageSize()), bottom, offset, serializer);
	}
	
	public Page(BinarySearchST<K, Long> st, boolean bottom, long offset, PageSerializer<K> serializer) {
		this.st = st;
		this.bottom = bottom;
		this.serializer = serializer;
		this.offset = offset;
	}

	
	public void insert(K key, Long offset) {
		st.put(key, offset);
	}

	
	public void enter(Page<K> p) {
		K min = p.st.min();
		st.put(min, p.offset);
	}

	
	public Page<K> next(K key) throws Exception {
		K next = st.floor(key);
		return serializer.read(st.get(next));
	}

	
	public Page<K> split() throws Exception {
		BinarySearchST<K, Long> newST = this.st.split();	
		this.close();
		return serializer.append(newST, bottom);
	}
	
	public Long getDataOffset(K key) {
		return st.get(key);
	}

	
	public boolean holds(K key) {
		return st.contains(key);
	}

	public boolean isExternal() {
		return bottom;
	}

	public boolean isOverflowed() {
		return st.size() == serializer.getPageSize();
	}

	public void close() throws Exception {
		serializer.write(offset, this);
	}
	
	public BinarySearchST<K, Long> asSymbolTable() {
		return st;
	}
}
