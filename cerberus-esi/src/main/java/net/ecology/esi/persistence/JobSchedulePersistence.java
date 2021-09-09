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
package net.ecology.esi.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.ecology.entity.scheduler.ScheduleJob;
import net.ecology.framework.persistence.CodePersistence;

/**
 * Repository interface for the {@link ScheduleJob} entity. It contains methods for
 * regular <code>CRUD</code> operations
 * 
 * @author bqduc
 *
 */
@Repository
@Transactional
public interface JobSchedulePersistence extends CodePersistence<ScheduleJob, Long> {
	/**
	 * Retrieves a {@link JobSchedule} entity from the underlying data store by its
	 * ResetKey
	 * 
	 * @param code
	 *            the login
	 * @return a User entity
	 * @see JobSchedule#getResetKey()
	 *//*
	JobSchedule findByCode(String code);*/

	/**
	 * Retrieves a {@link ScheduleJob} entity from the underlying datastore by its
	 * Email
	 * 
	 * @param name
	 *            the User's email
	 * @return a User entity
	 * @see ScheduleJob#getEmail()
	 */
	ScheduleJob findByName(String name);

	/**
	 * Check if a user with the ssoId exists in the system
	 * 
	 * @param code
	 *            the user account's ssoId
	 * @return a true if exists
	 */
	boolean existsByCode(String code);
}
