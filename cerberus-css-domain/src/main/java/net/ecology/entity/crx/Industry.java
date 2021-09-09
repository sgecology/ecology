package net.ecology.entity.crx;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * An region or CRX.
 */
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "industry")
@EqualsAndHashCode(callSuper=false)
public class Industry extends RepoObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7468751835306200560L;

	@Size(min = 5, max = 200)
	@Column(name = GlobeConstants.PROP_NAME, nullable = false, unique=true)
	private String name;

	@Column(name = "info", columnDefinition="TEXT")
	private String info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
