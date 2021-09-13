/**
 * 
 */
package net.ecology.model;

import java.util.Collection;

/**
 * @author ducbui
 *
 */
public interface IXContainer <T> {
	T get(Object key);
	T put(Object key, T value);

	Collection<Object> keys();
	Collection<T> values();
}
