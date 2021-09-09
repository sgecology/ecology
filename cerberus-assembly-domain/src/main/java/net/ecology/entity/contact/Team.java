package net.ecology.entity.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * A team.
 */
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "team")
@EqualsAndHashCode(callSuper=false)
public class Team extends RepoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9076437919776946515L;

	@Column(name = GlobeConstants.PROP_NAME, length=100)
	private String name;

	@Lob
	@Column(name = "info", columnDefinition = "TEXT")
	@Type(type = "org.hibernate.type.TextType")
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
