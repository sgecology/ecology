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

package net.ecology.entity.auth.base;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import net.ecology.framework.entity.Entity;

*//**
 * An abstract authentication account entity.
 * 
 * @author bqduc
 *//*
public interface PrincipalDetails extends UserDetails, Entity {
	void setUsername(String username);
	void setPassword(String password);

	String getEncryptedAlg();
	void setEncryptedAlg(String encryptedAlg);

	String getEmail();
	void setEmail(String email);

	String getToken();
	void setToken(String token);

	///////////////////////////////////////////////////////////////
	Short getExpirationPolicy();
	void setExpirationPolicy(Short expirationPolicy);

	Date getRegisteredDate();
	void setRegisteredDate(Date enabledDate);

	Date getEnabledDate();
	void setEnabledDate(Date enabledDate);
	//////////////////////////////////////////////////////////////

	//Boolean getSystemAdmin();
	//void setSystemAdmin(Boolean systemAdmin);

	String name();
	void name(String name);
}*/