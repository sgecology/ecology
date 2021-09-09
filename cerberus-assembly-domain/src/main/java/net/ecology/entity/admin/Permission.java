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
package net.ecology.entity.admin;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ecology.entity.auth.Authority;
import net.ecology.framework.entity.RepoObject;

/**
 * A user.
 * 
 * @author bqduc
 */
@Data
@Entity
@Table(name = "aux_permission")
@EqualsAndHashCode(callSuper = true)
public class Permission extends RepoObject{
	private static final long serialVersionUID = 3067524972341936718L;

	@ManyToOne
	private Authority authority;
	
	@ManyToOne
	private Division module;

	@ManyToOne
	private AccessRight accessRight;

	public Authority getAuthority() {
		return authority;
	}

	public Permission setAuthority(Authority authority) {
		this.authority = authority;
		return this;
	}

	public Division getModule() {
		return module;
	}

	public Permission setModule(Division module) {
		this.module = module;
		return this;
	}

	public AccessRight getAccessRight() {
		return accessRight;
	}

	public Permission setAccessRight(AccessRight accessRight) {
		this.accessRight = accessRight;
		return this;
	}

	public static Permission getInstance(Authority authority, Division module, AccessRight accessRight){
		return new Permission()
				.setAuthority(authority)
				.setModule(module)
				.setAccessRight(accessRight);
	}
}