package net.ecology.esi.service;

import java.util.Optional;

import net.ecology.entity.scheduler.SchedulePlan;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface SchedulePlanService extends IGenericService<SchedulePlan, Long> {

	/**
	 * Get one SchedulePlan with the provided code.
	 * 
	 * @param code
	 *            The SchedulePlan code
	 * @return The SchedulePlan
	 * @throws ObjectNotFoundException
	 *             If no such SchedulePlan exists.
	 */
	Optional<SchedulePlan> getByCode(String code) throws ObjectNotFoundException;
}
