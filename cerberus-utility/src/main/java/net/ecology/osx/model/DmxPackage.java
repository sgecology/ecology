/**
 * 
 */
package net.ecology.osx.model;

import java.util.Collection;
import java.util.Map;

import net.ecology.common.CollectionsUtility;

/**
 * @author ducbui
 *
 */
public abstract class DmxPackage <T>{
	private Map<Object, T> packages = CollectionsUtility.createMap();

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
