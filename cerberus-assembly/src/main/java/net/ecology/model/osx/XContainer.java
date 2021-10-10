/**
 * 
 */
package net.ecology.model.osx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.model.XWorkbook;

/**
 * @author bqduc
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XContainer {
  @Setter @Getter
	private OfficeSuiteTarget suiteTargeted;

	@Setter @Getter @Builder.Default
	private Map<Object, XWorkbook> workbooks = new HashMap<>();

  @Setter @Getter
	private List<Object> container;

	public XContainer(Object[] values) {
		this.container = new ArrayList<>();
		for (int idx = 0; idx < values.length; idx++) {
			this.container.add(values[idx]);
		}
	}

	public static XContainer instance() {
		XContainer dataBucket = new XContainer();
		return dataBucket;
	}

	public static XContainer getInstance(OfficeSuiteTarget suiteTargeted) {
		XContainer dataBucket = new XContainer();
		dataBucket.setSuiteTargeted(suiteTargeted);
		return dataBucket;
	}

	public Object getBucketedData(Object key){
		return workbooks.get(key);
	}

	public XContainer putAll(Map<Object, XWorkbook> values) {
		this.workbooks.putAll(values);
		return this;
	}

	public XContainer put(Object key, XWorkbook value) {
		this.workbooks.put(key, value);
		return this;
	}

	public Object get(Object key) {
		if (this.workbooks.containsKey(key))
			return this.workbooks.get(key);

		return null;
	}

	public boolean containsKey(Object key) {
		return this.workbooks.containsKey(key);
	}

	public Object pull(Object key) {
		if (this.workbooks.containsKey(key))
			return this.workbooks.remove(key);

		return null;
	}

	public Set<Object> getKeys(){
		return this.workbooks.keySet();
	}

  public Collection<XWorkbook> getValues(){
    return this.workbooks.values();
  }
	
}
