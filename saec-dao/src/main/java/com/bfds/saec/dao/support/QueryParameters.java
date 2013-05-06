package com.bfds.saec.dao.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class QueryParameters implements Iterable<Map.Entry<String, Object>> {
	private Map<String, Object> patrmeters = new HashMap<String, Object>();
	
	public boolean containsKey(final String key) {
		return patrmeters.containsKey(key);
	}
	
	public Object put(final String key, Object value) {
		if(containsKey(key)) {
			throw new IllegalArgumentException("Key exists:"+key);
		}
		return patrmeters.put(key, value);
	}
	
	public Object replace(final String key, Object value) {
		return patrmeters.put(key, value);
	}	
	
	Object get(String key) {
		return patrmeters.get(key);
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return patrmeters.entrySet().iterator();
	}
		
	
}
