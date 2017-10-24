package br.ufc.crateus.eda.btree;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.btree.dtypes.StringDT;

public class BTree<K extends Comparable<K>, V> {
	
	private Page<K> root;
	private PageSerializer<K> pageSerializer;
	private DataSerializer<V> dataSerializer;
	
	public BTree(PageSerializer<K> pageSerializer, DataSerializer<V> dataSerializer) throws Exception {
		this.pageSerializer = pageSerializer;
		this.dataSerializer = dataSerializer;
		this.root = pageSerializer.readRoot();
	}
	
	public V get(K key) throws Exception {
		return get(root, key);
	}
	
	private V get(Page<K> r, K key) throws Exception {
		if (r.isExternal()) {
			Long offset = r.getDataOffset(key);
			return dataSerializer.read(offset);
		}
		return get(r.next(key), key);
	}

	public boolean contains(K key) throws Exception {
		return contains(root, key);
	}
	
	private boolean contains(Page<K> r, K key) throws Exception {
		if (r.isExternal()) return r.holds(key);
		return contains(r.next(key), key);
	}
	
	public void put(K key, V value) throws Exception {
		put(root, key, value);
		if (root.isOverflowed()) {
			Page<K> newRoot = new Page<>(false, PageSerializer.ROOT_OFFSET, pageSerializer);  
			Page<K> right = root.split();
			Page<K> left = pageSerializer.append(root.asSymbolTable(), root.isExternal());
			
			newRoot.enter(left);
			newRoot.enter(right);
			newRoot.close();
			
			root = newRoot;
		}
	}
	
	public Iterable<K> keys() throws Exception {
		Queue<K> queue = new LinkedList<>();
		collect(root, queue);
		return queue;
	}
	
	private void collect(Page<K> r, Queue<K> queue) throws Exception {
		if (r.isExternal()) {
			for (K key : r.asSymbolTable().keys()) queue.add(key);
		}
		else {
			for (K key : r.asSymbolTable().keys()) collect(r.next(key), queue);
		}
	}
	
	private void put(Page<K> r, K key, V value) throws Exception {
				
		if (r.isExternal()) {
			if (!r.holds(key)) {
				long offset = dataSerializer.append(value);
				r.insert(key, offset);
				pageSerializer.keyInserted();
				r.close();
			}
			else dataSerializer.write(r.getDataOffset(key), value);
		}
		else {
			Page<K> next = r.next(key);
			put(next, key, value);
			if (next.isOverflowed()) {
				Page<K> tmp = next.split();
			
				r.enter(tmp);
				r.close();
			}
		}
	}
	
	public int size() {
		return pageSerializer.getNumberOfKeys();
	}
	
	public static void main(String[] args) throws Exception {
		StringDT keyDT = new StringDT(21);
		StringDT valDT = new StringDT(16);
		
		File keysFile = new File("/Users/disciplinas/Desktop/keys.dat");
		PageSerializer<String> ps = new PageSerializer<>(keysFile, keyDT, 4);
		
		File valuesFile = new File("/Users/disciplinas/Desktop/data.dat");
		DataSerializer<String> ds = new DataSerializer<>(valuesFile, valDT);
		
		BTree<String, String> st = new BTree<String, String>(ps, ds);

//        st.put("www.cs.princeton.edu", "128.112.136.12");
//        st.put("www.cs.princeton.edu", "128.112.136.11");
//        st.put("www.princeton.edu",    "128.112.128.15");
//        st.put("www.yale.edu",         "130.132.143.21");
//        st.put("www.simpsons.com",     "209.052.165.60");
//        st.put("www.apple.com",        "17.112.152.32");
//        st.put("www.amazon.com",       "207.171.182.16");
//        st.put("www.ebay.com",         "66.135.192.87");
//        st.put("www.cnn.com",          "64.236.16.20");
//        st.put("www.google.com",       "216.239.41.99");
//        st.put("www.nytimes.com",      "199.239.136.200");
//        st.put("www.microsoft.com",    "207.126.99.140");
//        st.put("www.dell.com",         "143.166.224.230");
//        st.put("www.slashdot.org",     "66.35.250.151");
//        st.put("www.espn.com",         "199.181.135.201");
//        st.put("www.weather.com",      "63.111.66.11");
//        st.put("www.yahoo.com",        "216.109.118.65");
//        st.put("www.crateus.ufc.br",     "200.19.190.7");
//        st.put("www.ufc.br",     		"200.17.41.185");
        
        for (String key : st.keys()) System.out.println(key);
        System.out.println(st.get("www.crateus.ufc.br"));
	}
	
}
