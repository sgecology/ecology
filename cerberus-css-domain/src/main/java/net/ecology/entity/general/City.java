package net.ecology.entity.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * A city.
 */
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "city")
@EqualsAndHashCode(callSuper=false)
public class City extends RepoObject {
	private static final long serialVersionUID = -292350370506500701L;

	@Column(name = GlobeConstants.PROP_NAME, nullable = false, unique=true, length=100)
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private City parent;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@Lob
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

	public City getParent() {
		return parent;
	}

	public void setParent(City parent) {
		this.parent = parent;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
