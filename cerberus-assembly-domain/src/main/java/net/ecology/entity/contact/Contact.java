/*
* Copyright 2017, Bui Quy Duc
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.ecology.entity.contact;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.common.CollectionsUtility;
import net.ecology.domain.general.PartnerType;
import net.ecology.domain.model.GenderType;
import net.ecology.entity.business.BusinessAccount;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.entity.general.Catalogue;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;

/**
 * A contact.
 * 
 * @author ducbq
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact")
@EqualsAndHashCode(callSuper=false)
public class Contact extends RepoObject {
	private static final long serialVersionUID = -7270200825637800417L;

  @Setter @Getter
  @Column(name="saluation", length=5)
  private String saluation;

  @Setter @Getter
  @Column(name=GlobeConstants.PROP_CODE, length=GlobalConstants.SIZE_SERIAL, unique=true)
  private String code;

  @Setter @Getter
	@Column(name="display_name", length=GlobalConstants.SIZE_NAME_SHORT)
	private String displayName;

  @Setter @Getter
  @ManyToOne
  @JoinColumn(name = "assistant_id")
  private Contact assistant;

  @Setter @Getter
  @ManyToOne
  @JoinColumn(name = "owner_id")
  private BusinessAccount owner;
  
  @Setter @Getter
  @Column(name="record_type", length=10)
  private String recordType; //Determines which pick list values are available for the record.

  @Setter @Getter
  @Column(name="first_name", length=50)
	private String firstName;

  @Setter @Getter
	@Column(name="last_name", length=70)
	private String lastName;

  @Setter @Getter
	@Column(name="email", length=120)
	private String email;

  @Setter @Getter
  @Column(name="fax", length=20)
  private String fax;

  @Setter @Getter
  @Column(name="phones", length=GlobalConstants.SIZE_STRING_SMALL)
  private String phones;

  @Setter @Getter
  @Column(name="emergency_phone", length=GlobalConstants.SIZE_STRING_SMALL)
  private String emergencyPhone;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "lead_source_id")
	private Catalogue leadSource;

  @Setter @Getter
  @Column(name="birth_date")
  private Date birthDate;

  @Setter @Getter
	@Column(name="birthplace", length=50)
	private String birthplace;

  @Setter @Getter
	@Column(name="gender")
  @Enumerated(EnumType.ORDINAL)
  private GenderType gender;

  @Setter @Getter
	@Column(name="partner_type")
  @Enumerated(EnumType.ORDINAL)
  private PartnerType partnerType;

  @Setter @Getter
	@Column(name="current_address", length=GlobalConstants.SIZE_ADDRESS_MEDIUM)
  private String currentAddress;

  @Setter @Getter
	@Column(name="billing_address", length=GlobalConstants.SIZE_ADDRESS_MEDIUM)
  private String billingAddress;

  @Setter @Getter
	@Column(name="shipping_address", length=GlobalConstants.SIZE_ADDRESS_MEDIUM)
  private String shippingAddress;

  @Setter @Getter
	@Column(name="permanent_address", length=GlobalConstants.SIZE_ADDRESS_MEDIUM)
  private String permanentAddress;
  
  @Setter @Getter
  @Column(name="source_system", length=GlobalConstants.SIZE_STRING_SMALL)
  private String sourceSystem;
  
  @Setter @Getter
	@Column(name="title", length=50)
	private String title;

  @Setter @Getter
	@Column(name="identity", length=15)
	private String identity;

  @Setter @Getter
	@Column(name="identity_issued_date")
	private Date identityIssuedDate;

  @Setter @Getter
	@Column(name="identity_expired_date")
	private Date identityExpiredDate;

  @Setter @Getter
	@Column(name="identity_issued_place", length=15)
	private String identityIssuedPlace;

  @Setter @Getter
	@Column(name="passport", length=15)
	private String passport;

  @Setter @Getter
	@Column(name="passport_issued_date")
	private Date passportIssuedDate;

  @Setter @Getter
	@Column(name="passport_expired_date")
	private Date passportExpiredDate;

  @Setter @Getter
	@Column(name="passport_issued_place", length=15)
	private String passportIssuedPlace;

  @Setter @Getter
  @Builder.Default
  @OneToMany(
  		mappedBy="manager"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      //, fetch = FetchType.EAGER
  )
	private List<ContactHierarchy> hierarchies = CollectionsUtility.newList();

  @Setter @Getter
  @Builder.Default
  @OneToMany(
  		mappedBy="owner"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      //, fetch = FetchType.EAGER
  )
	private List<ContactAssignment> assignments = CollectionsUtility.newList();

  @Setter @Getter
	@Builder.Default
	@Transient
	private Set<ContactTeam> contactTeams = CollectionsUtility.newHashSet();

  @Setter @Getter
	@Builder.Default
	@Transient
	private Set<ContactDocument> documents = CollectionsUtility.newHashSet();

  @Setter @Getter
	@ManyToOne
	@JoinColumn(name = "proxy_id")
	private Contact proxy;

  @Setter @Getter
	@ManyToOne
	@JoinColumn(name = "introducer_id")
	private Contact introducer;

  @Setter @Getter
  @Column(name="issue_date")
  private Date issueDate;

	@Setter @Getter
	@Column(name="activation_key", length=20)
	@JsonIgnore
	private String activationKey;

  @Setter @Getter
	@Column(name="activation_date")
	private Date activationDate;
	
  @Setter @Getter
	@ManyToOne
	@JoinColumn(name = "business_unit_id")
	private BusinessUnit businessUnit;

  @Setter @Getter
	@ManyToOne
	@JoinColumn(name = "referal_id")
	private Contact referal;

  @Setter @Getter
	@ManyToOne
	@JoinColumn(name = "job_info_id")
	private Catalogue jobInfo;	

  @Setter @Getter
	@Lob
	@Column(name = "info", columnDefinition = "TEXT")
	@Type(type = "org.hibernate.type.TextType")
	private String info;

  @Setter @Getter
  @Column(name="country", length=30)
	private String country;

  @Setter @Getter
  @Column(name="marital_status", length=10)
	private String maritalStatus;

  @Setter @Getter
  @Column(name="qualifications", length=50)
	private String qualifications;

  @Setter @Getter
  @Column(name="foreign_languages", length=15)
	private String foreignLanguages;

  @Setter @Getter
  @Column(name="experience_level", length=20)
	private String experienceLevel;
  /*
   *1. Entry-level
   *2. Intermediate
   *2. Mid-level
   *3. Senior or executive-level
   **/

  @Setter @Getter
  @Column(name="experience", length=2)
	private String experience;//in years

  @Setter @Getter
  @Column(name="desired_profession", length=50)
	private String desiredProfession;

  @Setter @Getter
  @Column(name="religion", length=30)
	private String religion;

  @Setter @Getter
  @Column(name="ethnicity", length=30)
	private String ethnicity;
  
	@Setter @Getter
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(name="profile_picture")
  private byte[] profilePicture;

	@Transient
	public String name() {
		return new StringBuilder()
		.append(this.firstName)
		.append(GlobeConstants.NAME_SEPARATOR)
		.append(this.lastName)
		.toString();
	}

	@Transient
	public void name(String name) {
		if (!name.contains(GlobeConstants.NAME_SEPARATOR))
			return;

		String[] nameParts = name.split(GlobeConstants.NAME_SEPARATOR);
		this.firstName = nameParts[0];
		this.lastName = nameParts[1];
	}
}
