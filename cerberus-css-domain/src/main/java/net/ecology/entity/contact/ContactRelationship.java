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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.entity.general.Catalogue;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobalConstants;

/**
 * A contact relationship.
 * 
 * @author Bui Quy Duc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_relationship")
@EqualsAndHashCode(callSuper=false)
public class ContactRelationship extends RepoObject {
	private static final long serialVersionUID = 3907667611166708671L;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Contact participant;

	@ManyToOne
	@JoinColumn(name = "reportee_id")
	private Contact partner;

	@ManyToOne
	@JoinColumn(name = "relation_catalogue_id")
	private Catalogue relation;

	@Column(name="issue_date")
  private Date issueDate;

  @Column(name="issue_no", length=GlobalConstants.SIZE_STRING_TINY)
  private String issueNo;

  @Column(name="issue_place", length=GlobalConstants.SIZE_ADDRESS)
  private String issuePlace;

	@Lob
	@Column(name = "info", columnDefinition = "TEXT")
	@Type(type = "org.hibernate.type.TextType")
	private String info;
}
