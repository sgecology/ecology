/**
 * 
 */
package net.ecology.repository;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.springframework.stereotype.Component;

import net.ecology.common.BeanUtility;
import net.ecology.common.CollectionsUtility;
import net.ecology.domain.JobDescriptor;
import net.ecology.domain.JobSpec;
import net.ecology.domain.TriggerDescriptor;
import net.ecology.entity.scheduler.Schedule;
import net.ecology.global.SchedulingConstants;

/**
 * @author ducbq
 *
 */
@Component
public class JobScheduleRepository {
  Map<String, String> triggerSpecs = CollectionsUtility.newHashedMap(      
      "ReconcileAuthServices",    "5 0/2 * * * ?",
      "SyncGeneralServices",      "10 0/3 * * * ?",
      "AquariumReminders",        "2 0/1 * * * ?",
      "TransportationGenerators", "0 0/4 * * * ?",
      "SyncPOSServices",          "3 0/5 * * * ?"
  );

  @SuppressWarnings("unchecked")
  public List<TriggerDescriptor> getDefaultTriggerDescriptors(String group) throws ClassNotFoundException{    
    List<TriggerDescriptor> triggerDescriptors = CollectionsUtility.newList();
    JobDetail currJobDetail = null;
    String jobPackageNameQualified = "net.brilliant.scheduler.";
    for (JobSpec jobSpec :JobSpec.values()){
      currJobDetail = JobDescriptor.buildJobDetail(
          jobSpec.getGroup(), 
          jobSpec.getName() + SchedulingConstants.specJob, 
          (Class<? extends Job>)BeanUtility.getClass(jobPackageNameQualified + jobSpec.getName() + SchedulingConstants.specJob));

      triggerDescriptors.add(TriggerDescriptor.buildDescriptor(
          jobSpec.getName() + SchedulingConstants.specTrigger, 
          group, 
          null, 
          jobSpec.getCronExpression(), 
          currJobDetail)
       );
    }
    return triggerDescriptors;
  }

  @SuppressWarnings("unchecked")
  public List<JobDescriptor> generateJobDescriptors() throws ClassNotFoundException{    
    List<JobDescriptor> jobDescriptors = CollectionsUtility.newList();
    JobDetail currJobDetail = null;
    String jobPackageNameQualified = "net.brilliant.scheduler.";
    TriggerDescriptor triggerDescriptor;
    for (JobSpec jobSpec :JobSpec.values()){
      currJobDetail = JobDescriptor.buildJobDetail(
          jobSpec.getGroup(), 
          jobSpec.getName() + SchedulingConstants.specJob, 
          (Class<? extends Job>)BeanUtility.getClass(jobPackageNameQualified + jobSpec.getName() + SchedulingConstants.specJob));

      triggerDescriptor = TriggerDescriptor.buildDescriptor(
          jobSpec.getName() + SchedulingConstants.specTrigger, 
          SchedulingConstants.defaultGroup, 
          null, 
          jobSpec.getCronExpression(), 
          currJobDetail);

      jobDescriptors.add(JobDescriptor.buildDescriptor(currJobDetail, CollectionsUtility.newList(triggerDescriptor.buildTrigger())));
    }
    return jobDescriptors;
  }

  @SuppressWarnings("unchecked")
  public List<JobDescriptor> generateJobDescriptors(List<Schedule> jobSchedules){    
    List<JobDescriptor> jobDescriptors = CollectionsUtility.newList();
    JobDetail currJobDetail = null;
    TriggerDescriptor triggerDescriptor;
    Class<? extends Job> jobScheduleClass = null;
    for (Schedule jobSchedule :jobSchedules){
      try {
        jobScheduleClass = (Class<? extends Job>)BeanUtility.getClass(jobSchedule.getJobClass());
      } catch (Exception e) {
        jobScheduleClass = null;
      }

      if (null==jobScheduleClass)
        continue;

      currJobDetail = JobDescriptor.buildJobDetail(
          jobSchedule.getSchedulePlan().getJobType(), 
          jobSchedule.getSchedulePlan().getName(), 
          jobScheduleClass);

      triggerDescriptor = TriggerDescriptor.buildDescriptor(
          jobScheduleClass.getName(), 
          jobSchedule.getCategory(), 
          null, 
          jobSchedule.getCronExpression(), 
          currJobDetail);

      jobDescriptors.add(JobDescriptor.buildDescriptor(currJobDetail, CollectionsUtility.newList(triggerDescriptor.buildTrigger())));
    }
    return jobDescriptors;
  }
}