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
public class SalesJobSchedule extends JobSchedulerBase {
  protected void executing(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    super.reportScheduleStatus(this.getClass().getSimpleName());
  }
}
