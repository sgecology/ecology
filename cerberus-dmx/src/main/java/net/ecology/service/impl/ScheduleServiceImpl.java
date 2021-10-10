package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.scheduler.Schedule;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.persistence.SchedulePersistence;
import net.ecology.service.ScheduleService;


@Service
public class ScheduleServiceImpl extends GenericService<Schedule, Long> implements ScheduleService {
	/**
   * 
   */
  private static final long serialVersionUID = 7619068697797499651L;
  @Inject 
	private SchedulePersistence repository;
	
	protected IPersistence<Schedule, Long> getPersistence() {
		return this.repository;
	}
}
