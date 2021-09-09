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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.entity.general.Catalogue;
import net.ecology.framework.entity.RepoObject;

/**
 * A contact.
 * 
 * @author Bui Quy Duc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_assignment")
@EqualsAndHashCode(callSuper=false)
public class ContactAssignment extends RepoObject {
	private static final long serialVersionUID = -5471626820797268946L;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Contact owner;

	@ManyToOne
	@JoinColumn(name = "proxy_contact_id")
	private Contact proxy;

	@ManyToOne
	@JoinColumn(name = "direct_id")
	private BusinessUnit businessUnit;

	@ManyToOne
	@JoinColumn(name = "designation_catalogue_id")
	private Catalogue designation;

	@ManyToOne
	@JoinColumn(name = "job_info_catalogue_id")
	private Catalogue jobInfo;
	
	@Column(name="issued_date")
  private Date issuedDate;

  @Column(name="expired_date")
  private Date expiredDate;

}
