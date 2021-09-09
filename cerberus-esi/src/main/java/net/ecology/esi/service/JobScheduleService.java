package net.ecology.esi.service;

import java.util.Optional;

import net.ecology.entity.scheduler.ScheduleJob;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface JobScheduleService extends IGenericService<ScheduleJob, Long> {

	/**
	 * Get one JobSchedule with the provided code.
	 * 
	 * @param code
	 *            The JobSchedule code
	 * @return The JobSchedule
	 * @throws ObjectNotFoundException
	 *             If no such JobSchedule exists.
	 */
	Optional<ScheduleJob> getByCode(String code) throws ObjectNotFoundException;
}
