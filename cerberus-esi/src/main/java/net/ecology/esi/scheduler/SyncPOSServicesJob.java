/**
 * 
 */
package net.ecology.esi.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.ecology.esi.base.JobSchedulerBase;

/**
 * @author ducbq
 *
 */
public class SyncPOSServicesJob extends JobSchedulerBase {
  protected void executing(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    super.reportScheduleStatus(this.getClass().getSimpleName());
  }
}
