package net.ecology.entity.contact;
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

package net.paramount.entity.contact;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.paramount.auth.entity.UserPrincipal;
import net.paramount.common.CollectionsUtility;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.entity.general.Catalogue;
import net.paramount.framework.entity.RepoObject;

*//**
 * A contact profile - detailed of contact.
 * 
 * @author Bui Quy Duc
 *//*
@Builder
@Data
@Entity
@Table(name = "contact_profile")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContactProfile extends RepoObject {
	private static final long serialVersionUID = -4848362258780874163L;

	@ManyToOne
	@JoinColumn(name = "contact_id")
	private Contact contact;

	@Builder.Default
  @OneToMany(
  		mappedBy="owner"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      //, fetch = FetchType.EAGER
  )
	private List<ContactAddress> addresses = CollectionsUtility.newList();

	@Builder.Default
  @OneToMany(
  		mappedBy="owner"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      //, fetch = FetchType.EAGER
  )
	private List<ContactCommunication> communications = CollectionsUtility.newList();

  @Builder.Default
  @OneToMany(
  		mappedBy="owner"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      //, fetch = FetchType.EAGER
  )
	private List<ContactRelation> hierarchies = CollectionsUtility.newList();

  @Builder.Default
  @OneToMany(
  		mappedBy="owner"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      //, fetch = FetchType.EAGER
  )
	private List<ContactAssignment> assignments = CollectionsUtility.newList();

	@Transient
	@Builder.Default
	private Set<ContactAddress> contactAddresses = CollectionsUtility.newHashSet();

	@Builder.Default
	@Transient
	private Set<ContactTeam> contactTeams = CollectionsUtility.newHashSet();
	  
	@ManyToOne
	@JoinColumn(name = "proxy_contact_id")
	private Contact proxy;

	@ManyToOne
	@JoinColumn(name = "introducer_contact_id")
	private Contact introducer;

  @Column(name="issue_date")
  private Date issueDate;
	
	@ManyToOne
  @JoinColumn(name="issuer_id")
  private UserPrincipal issuedBy;

	@Column(name="activation_key", length=20)
	@JsonIgnore
	private String activationKey;

	@Column(name="reset_key", length=20)
	@JsonIgnore
	private String resetKey;

	@Column(name="reset_date")
	private Date resetDate;
	
  @Column(name="portal_name", length=50)
	private String portalName;

	@JsonIgnore
	@Column(name="portal_secret_key", length=50)
	private String portalSecretKey;

	@Builder.Default
	@Column(name = "portal_active")
	private Boolean portalActive = false;

	@ManyToOne
	@JoinColumn(name = "sms_opt_in_id")
	private Catalogue smsOptIn;

	@ManyToOne
	@JoinColumn(name = "business_unit_id")
	private BusinessUnit businessUnit;

	@ManyToOne
	@JoinColumn(name = "referal_id")
	private Contact referal;

	@Builder.Default
	@Column(name = "sync_contact")
	private Boolean syncContact = false;

	@Builder.Default
	@Column(name = "do_not_call")
	private Boolean doNotCall = false;

	@Builder.Default
	@Column(name = "email_opt_out")
	private Boolean emailOptOut = false;

	@ManyToOne
	@JoinColumn(name = "job_info_id")
	private Catalogue jobInfo;
}
*/