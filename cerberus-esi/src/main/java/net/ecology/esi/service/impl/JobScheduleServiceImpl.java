package net.ecology.esi.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.scheduler.ScheduleJob;
import net.ecology.esi.persistence.JobSchedulePersistence;
import net.ecology.esi.service.JobScheduleService;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class JobScheduleServiceImpl extends GenericService<ScheduleJob, Long> implements JobScheduleService {
	/**
   * 
   */
  private static final long serialVersionUID = 7619068697797499651L;
  @Inject 
	private JobSchedulePersistence repository;
	
	protected IPersistence<ScheduleJob, Long> getPersistence() {
		return this.repository;
	}
}
