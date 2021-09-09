/**
 * 
 */
package net.ecology.model;

import java.util.Collection;

/**
 * @author ducbui
 *
 */
public interface DataContainer <T> {
	T get(Object key);
	T put(Object key, T value);

	Collection<Object> keys();
	Collection<T> values();
}
