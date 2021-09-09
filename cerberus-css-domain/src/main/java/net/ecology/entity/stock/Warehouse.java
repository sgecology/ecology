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

package net.ecology.entity.stock;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import net.ecology.embeddable.Address;
import net.ecology.entity.contact.ContactPerson;
import net.ecology.entity.general.City;
import net.ecology.entity.general.Organization;
import net.ecology.entity.general.Province;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * Entity class Warehouse
 * 
 * @author haky
 */
@Entity
@Table(name = "WAREHOUSE")
public class Warehouse extends RepoObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "CODE", length = 20, nullable = false, unique = true)
	@Size(max = 20, min = 1)
	private String code;

	@Column(name = GlobeConstants.PROP_NAME, length = 50)
	@Size(max = 50, min = 1)
	private String name;

	@Column(name = "INFO")
	private String info;

	@Embedded
	private Address address = new Address();

	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;

	@ManyToOne
	@JoinColumn(name = "PROVINCE_ID")
	private Province province;

	// yetkili kisi
	@Embedded
	private ContactPerson contactPerson;

	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID")
	private Organization organization;

	@Override
	public String toString() {
		return "Warehouse[id=" + getId() + "]";
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public ContactPerson getContactPerson() {
		if (contactPerson == null) {
			contactPerson = new ContactPerson();
		}
		return contactPerson;
	}

	public void setContactPerson(ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
	}

}
