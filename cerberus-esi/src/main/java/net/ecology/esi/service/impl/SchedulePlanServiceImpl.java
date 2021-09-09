package net.ecology.esi.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.scheduler.SchedulePlan;
import net.ecology.esi.persistence.SchedulePlanPersistence;
import net.ecology.esi.service.SchedulePlanService;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class SchedulePlanServiceImpl extends GenericService<SchedulePlan, Long> implements SchedulePlanService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7409137083581193634L;

	@Inject 
	private SchedulePlanPersistence repository;
	
	protected IPersistence<SchedulePlan, Long> getPersistence() {
		return this.repository;
	}
}
