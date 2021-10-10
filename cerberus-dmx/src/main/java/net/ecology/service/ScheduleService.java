package net.ecology.service;

import java.util.Optional;

import net.ecology.entity.scheduler.Schedule;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface ScheduleService extends IGenericService<Schedule, Long> {

	/**
	 * Get one JobSchedule with the provided code.
	 * 
	 * @param code
	 *            The JobSchedule code
	 * @return The JobSchedule
	 * @throws ObjectNotFoundException
	 *             If no such JobSchedule exists.
	 */
	Optional<Schedule> getByCode(String code) throws ObjectNotFoundException;
}
