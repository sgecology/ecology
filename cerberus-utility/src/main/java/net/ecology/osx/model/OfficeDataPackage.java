/**
 * 
 */
package net.ecology.osx.model;

import java.util.Map;

import lombok.Builder;
import net.ecology.common.CollectionsUtility;
import net.ecology.osx.model.OfficeDataPackage;

/**
 * @author bqduc
 *
 */
@Builder
public class OfficeDataPackage extends DmxPackage {
	@Builder.Default
	private Map<Object, DmxPackage> packageData = CollectionsUtility.createHashMapData();

	public DmxPackage getDataPackage(final Object key){
		return packageData.get(key);
	}

	public OfficeDataPackage putAll(final Map<Object, ? extends DmxPackage> values) {
		this.packageData.putAll(values);
		return this;
	}

	public OfficeDataPackage put(final Object key, final DmxPackage value) {
		this.packageData.put(key, value);
		return this;
	}
}
