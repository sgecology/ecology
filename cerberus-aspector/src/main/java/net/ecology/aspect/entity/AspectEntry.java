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
package net.ecology.aspect.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.framework.entity.Repo;
import net.ecology.global.GlobalConstants;

/**
 * An item.
 * 
 * @author bqduc
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aspect_entry")
@EqualsAndHashCode(callSuper = true)
public class AspectEntry extends Repo {
  private static final long serialVersionUID = -6469482561701512558L;

  @Setter @Getter
  private Long objectId;

  @Setter @Getter
  @Column(name="object", length=GlobalConstants.SIZE_STRING_SMALL, nullable=false)
  private String object;

  @Setter @Getter
  @Column(length=GlobalConstants.SIZE_STRING_TINY, nullable=false)
  private String action;

  @Setter @Getter
	@Column
	private Date date;

  @Setter @Getter
  @Column (name="user_id")
  private Long userId;
}
