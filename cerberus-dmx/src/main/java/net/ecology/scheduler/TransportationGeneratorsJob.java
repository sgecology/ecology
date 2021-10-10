/**
 * 
 */
package net.ecology.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.ecology.base.JobSchedulerBase;

/**
 * @author ducbq
 *
 */
public class TransportationGeneratorsJob extends JobSchedulerBase {
  protected void executing(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    super.reportScheduleStatus(this.getClass().getSimpleName());
  }
}
