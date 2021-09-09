/**
 * 
 */
package net.ecology.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ducbui
 *
 */
public /*abstract */class XPackage <T>{
	private Map<Object, T> packages = new HashMap<>();

	public T get(Object key){
		return packages.get(key);
	}

	public T put(Object key, T value){
		this.packages.put(key, value);
		return value;
	}

	public Collection<Object> keys(){
		return packages.keySet();
	}

	public Collection<T> values(){
		return packages.values();
	}
}
