/**
 * 
 */
package net.ecology.osx.model;

import java.util.Collection;

/**
 * @author ducbui
 *
 */
public interface DmxContainer <T/* extends DataObject*/> {
	T get(Object key);
	T put(Object key, T value);

	Collection<Object> keys();
	Collection<T> values();
}
