package net.ecology.controller.admin;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.ecology.entity.scheduler.Schedule;
import net.ecology.framework.controller.BrowserHome;
import net.ecology.framework.model.NameFilter;
import net.ecology.service.ScheduleService;

/**
 * @author ducbq
 */
@Named
@ViewScoped
public class JobSchedulerBrowse extends BrowserHome<Schedule, NameFilter> {
  /**
   * 
   */
  private static final long serialVersionUID = 1754644079582592185L;
  private static final String cachedDataProp = "cachedJobDescriptors";

  @Inject
  private ScheduleService businessService;

  @Override
  public NameFilter createFilterModel() {
    return new NameFilter();
  }

  @Override
  protected List<Schedule> requestBusinessObjects() {
    List<Schedule> requestedBusinessObjects = (List<Schedule>)this.fetchCachedData(cachedDataProp);
    if (null == requestedBusinessObjects) {
      requestedBusinessObjects = businessService.getObjects();
      this.cachePut(cachedDataProp, requestedBusinessObjects);
    }
    return requestedBusinessObjects;
  }
}
