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
package net.ecology.entity.business;

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
@Table(name = "account_relation")
@EqualsAndHashCode(callSuper=false)
public class BusinessAccountRelation extends RepoObject {
  private static final long serialVersionUID = -2914985785892671407L;

  @ManyToOne
	@JoinColumn(name = "source_id")
	private BusinessAccount source;

	@ManyToOne
	@JoinColumn(name = "target_id")
	private BusinessAccount target;

  @Column(name="issuer_id")
  private Long issuerId;

  @Column(name="catalog_relation_id")
  private Long relationId;
}
