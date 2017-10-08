package com.vibridi.rblock.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockableRecord {

	public static Map<String,BlockableRecord> toMap(Collection<Map<String,String>> records, String keyField) {
		return records.stream()
				.map(m -> new BlockableRecord(m, keyField))
				.collect(Collectors.toMap(BlockableRecord::getKey, Function.identity()));
	}
	
	public static Map<String,BlockableRecord> toMap(List<BlockableRecord> records) {
		return records.stream().collect(Collectors.toMap(BlockableRecord::getKey, Function.identity()));
	}
	
	private Map<String,String> data; // database record represented in a key-value format
	private String keyField;
	
	public BlockableRecord() {
		data = new HashMap<>();
		keyField = null;
	}

	public BlockableRecord(Map<String,String> data, String keyField) {
		if(!data.containsKey(keyField) || data.get(keyField) == null) {
			throw new IllegalStateException("Record data must include the primary key and its value");
		}
		this.data = data;
		this.keyField = keyField;
	}
	
	/**
	 * Compares two <code>BlockableRecord</code> objects. <br>
	 * Warning: in order to speed up comparison, it only tests for equality between primary keys. 
	 * The user is responsible of making sure that two records with the same PK also have the same field values.<br><br>
	 * This is consistent with the <code>Pair</code> class, which also tests equality between PK values.
	 * 
	 * @see Pair
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BlockableRecord) {
			BlockableRecord that = (BlockableRecord) obj;
			return this.data.get(this.keyField).equals(that.data.get(that.keyField));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.data.get(keyField).hashCode();
	}
	
	public boolean put(String field, String value) {
		if(field.equals(keyField))
			throw new IllegalArgumentException("Can not overwrite the record's primary key");
		return data.put(field, value) == null;
	}
	
	public String getKeyField() {
		return this.keyField;
	}
	
	public String getKey() {
		return this.data.get(keyField);
	}
	
	public Map<String,String> getData() {
		return Collections.unmodifiableMap(data);
	}
}
