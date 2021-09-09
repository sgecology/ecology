/**
 * 
 */
package net.ecology.model.base;

import java.util.List;

/**
 * @author bqduc
 *
 */
public interface IDataContainer<T> {
	List<T> getHeaderItems();
	DataList<List<T>> getDataItems();

	void addHeaderItems(List<T> headerItems);
	void addDataItems(List<T> dataItems);
}
