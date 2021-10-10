/*
* Copyright 2021, BQDuc
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
package net.ecology.entity.business;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.domain.general.PartnerType;
import net.ecology.domain.model.GenderType;
import net.ecology.embeddable.Address;
import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;

/**
 * A business account.
 * 
 * @author BQDuc
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_account")
public class BusinessAccount extends RepoObject {
  private static final long serialVersionUID = -4837025193883409423L;

  @Setter @Getter
  @Column(name=GlobeConstants.PROP_CODE, length=GlobalConstants.SIZE_SERIAL, unique=true)
  private String code;

  @Setter @Getter
	@Column(name="name", length=GlobalConstants.SIZE_NAME_SHORT)
	private String name;

  @Setter @Getter
  @ManyToOne
  @JoinColumn(name = "assistant_id")
  private BusinessAccount assistant;

  @Setter @Getter
  @ManyToOne
  @JoinColumn(name = "owner_id")
  private BusinessAccount owner;
  
  @Setter @Getter
  @Column(name="record_type", length=10)
  private String recordType; //Determines which pick list values are available for the record.

  @Setter @Getter
  @Column(name="department", length=30)
  private String department;
  
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
  @Column(name="home_phone", length=20)
  private String homePhone;

  @Setter @Getter
  @Column(name="mobile_phone", length=20)
  private String mobilePhone;

  @Setter @Getter
  @Column(name="emergency_phone", length=20)
  private String emergencyPhone;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "lead_source_id")
	private GeneralCatalogue leadSource;

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
	@Column(name="type")
  @Enumerated(EnumType.ORDINAL)
  private PartnerType type;

  @Setter @Getter
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="address", column=@Column(name="address", length=GlobalConstants.SIZE_ADDRESS)),
    @AttributeOverride(name="city", column=@Column(name="city", length=GlobalConstants.SIZE_CITY)),
    @AttributeOverride(name="state", column=@Column(name="state", length=GlobalConstants.SIZE_STRING_SMALL)),
    @AttributeOverride(name="postalCode", column=@Column(name="postal_code", length=10)),
    @AttributeOverride(name="country", column=@Column(name="country", length=15)) //Country code
  })
  private Address address;

  @Setter @Getter
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="address", column=@Column(name="mailing_address", length=GlobalConstants.SIZE_ADDRESS)),
    @AttributeOverride(name="city", column=@Column(name="mailing_city", length=GlobalConstants.SIZE_CITY)),
    @AttributeOverride(name="state", column=@Column(name="mailing_state", length=GlobalConstants.SIZE_STRING_SMALL)),
    @AttributeOverride(name="postalCode", column=@Column(name="mailing_postal_code", length=10)),
    @AttributeOverride(name="country", column=@Column(name="mailing_country", length=15)) //Country code
  })
  private Address mailingAddress;

  @Setter @Getter
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="address", column=@Column(name="other_address", length=GlobalConstants.SIZE_ADDRESS)),
    @AttributeOverride(name="city", column=@Column(name="other_city", length=GlobalConstants.SIZE_CITY)),
    @AttributeOverride(name="state", column=@Column(name="other_state", length=GlobalConstants.SIZE_STRING_SMALL)),
    @AttributeOverride(name="postalCode", column=@Column(name="other_postal_code", length=10)),
    @AttributeOverride(name="country", column=@Column(name="other_country", length=15)) //Country code
  })
  private Address otherAddress;
  
  @Setter @Getter
	@Lob @Type(type = "org.hibernate.type.TextType")
	@Column(name = "info", columnDefinition = "TEXT")
	private String info;

  @Setter @Getter
  @Column(name="issued_date")
  private Date issuedDate;

  @Setter @Getter
  @Column(name="issuer_id")
  private Long issuerId;

  @Column(name="saluation", length=5)
  private String saluation;

  @Column(name="source_system", length=GlobalConstants.SIZE_STRING_SMALL)
  private String sourceSystem;
}
