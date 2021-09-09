/*
* Copyright 2018, Bui Quy Duc
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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.embeddable.Address;
import net.ecology.framework.entity.RepoObject;

/**
 * A contact base abstract class.
 * 
 * @author Bui Quy Duc
 */
@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "contact_address")
@EqualsAndHashCode(callSuper = true)
public class ContactAddress extends RepoObject {
	private static final long serialVersionUID = 352122883509888259L;

	@Embedded
  @AttributeOverrides({
    @AttributeOverride(name="address", column=@Column(name="address")),
    @AttributeOverride(name="city", column=@Column(name="city")),
    @AttributeOverride(name="state", column=@Column(name="state")),
    @AttributeOverride(name="postalCode", column=@Column(name="postal_code")),
    @AttributeOverride(name="country", column=@Column(name="country"))
  })
  private Address address;

	@ManyToOne
	@JoinColumn(name = "contact_id")
	private Contact owner;
}
