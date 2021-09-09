/*
 * Copyleft 2007-2011 Ozgur Yazilim A.S.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * www.tekir.com.tr
 * www.ozguryazilim.com.tr
 *
 */

package net.ecology.entity.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 *
 * @author sinan.yumak
 *
 */
@Entity
@Table(name = "ORGANIZATION_LEVEL")
public class OrganizationLevel extends RepoObject {

	private static final long serialVersionUID = 1L;

	@Column(name = GlobeConstants.PROP_NAME)
	private String name;

	@Column(name = "LEVEL")
	private Integer level;

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

}
