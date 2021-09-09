/**
 * 
 */
package net.ecology.model.ui;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.ecology.global.GlobeConstants;

/**
 * @author bqduc
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UISelectItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6777889120185884326L;

	private Long id;
	private String code;
	private String name;
	private String nameLocal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UISelectItem instance(Long key, Map<String, Object> properties){
		return UISelectItem.builder()
				.id(key)
				.code((String)properties.get(GlobeConstants.PROP_CODE))
				.name(properties.containsKey(GlobeConstants.PROP_NAME)?(String)properties.get(GlobeConstants.PROP_NAME):"")
				.nameLocal(properties.containsKey(GlobeConstants.PROPERTY_NAME_LOCAL)?(String)properties.get(GlobeConstants.PROPERTY_NAME_LOCAL):"")
				.build();
	}

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}
}