/**
 * 
 */
package net.ecology.esi.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.inject.Inject;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.ecology.base.Marshaller;
import net.ecology.common.BeanUtility;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.entity.scheduler.ScheduleJob;
import net.ecology.entity.scheduler.SchedulePlan;
import net.ecology.esi.domain.JobDescriptor;
import net.ecology.esi.domain.TriggerDescriptor;
import net.ecology.esi.service.JobScheduleService;
import net.ecology.esi.service.SchedulePlanService;
import net.ecology.exceptions.CerberusException;
import net.ecology.global.GlobeConstants;
import net.ecology.global.SchedulingConstants;
import net.ecology.marshal.ScheduleJobMarshaller;
import net.ecology.marshal.SchedulePlanMarshaller;
import net.ecology.model.Context;

/**
 * @author ducbq
 *
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class SchedulerServicesHelper {
  @Inject
  private Scheduler scheduler;

  @Inject
  private JobScheduleService jobScheduleService;

  @Inject
  private SchedulePlanService schedulePlanService;

  private StdSchedulerFactory schedulerFactory;
  List<TriggerDescriptor> triggerDescriptors;

  @Setter @Getter
  private List<JobDescriptor> jobDescriptors;

  @Async
  private void dispatch(Context context) throws CerberusException {
    log.info("Enter startSchedulerEngine()");

    List<ScheduleJob> jobSchedules = parseJobSchedules(null);//(context);
    if (CommonUtility.isEmpty(jobSchedules)){
      log.info("There is no job schedule to be run. ");
      return;
    }

    JobDetail currentJobDetail = null;
    Class<? extends Job> jobScheduleClass = null;
    Trigger currentTrigger = null;
    try {
      this.schedulerFactory = new StdSchedulerFactory();
      Scheduler scheduler = this.schedulerFactory.getScheduler();

      for (ScheduleJob jobSchedule :jobSchedules) {
        if (CommonUtility.isEmpty(jobSchedule.getJobClass()))
          continue;

        try {
          jobScheduleClass = (Class<? extends Job>)BeanUtility.getClass(jobSchedule.getJobClass());
        } catch (Exception e) {
          jobScheduleClass = null;
          log.error("An error occurred while get associated class from: " + jobSchedule.getJobClass());
        }

        if (null==jobScheduleClass)
          continue;

        currentTrigger = buildTrigger(jobSchedule.getName() + SchedulingConstants.specTrigger, jobSchedule.getCategory(), null, jobSchedule.getCronExpression());
        
        currentJobDetail = JobBuilder
            .newJob(jobScheduleClass)
            .withIdentity(jobSchedule.getName(), jobSchedule.getCategory())
            .build();

        scheduler.scheduleJob(currentJobDetail, currentTrigger);
      }

      scheduler.start();
		} catch (Exception e) {
			throw new CerberusException(e);
		}
    log.info("Leave startSchedulerEngine()");
  }

  public List<TriggerDescriptor> getTriggerDescriptors(String status) throws SchedulerException{
    if (CommonUtility.isNotEmpty(this.triggerDescriptors))
      return this.triggerDescriptors;

    if (null==this.triggerDescriptors){
      this.triggerDescriptors = CollectionsUtility.newList();
    }

    Scheduler scheduler = this.schedulerFactory.getScheduler();
    for (String groupName : scheduler.getJobGroupNames()) {
      for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
        String jobName = jobKey.getName();
        String jobGroup = jobKey.getGroup();
         //get job's trigger
        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        Date nextFireTime = triggers.get(0).getNextFireTime(); 
        System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
        for (Trigger trigger :triggers){
          triggerDescriptors.add(TriggerDescriptor.buildDescriptor(trigger));
        }
      }
     }
    return triggerDescriptors;
  }

  private List<ScheduleJob> parseJobSchedules(List<String[]> dataElements){
    List<ScheduleJob> loadedJobSchedules = CollectionsUtility.newList();
    ScheduleJob jobSchedule = null;
    Marshaller<SchedulePlan, String[]> schedulePlanMarshaller = SchedulePlanMarshaller.builder().build();
    Marshaller<ScheduleJob, String[]> scheduleJobMarshaller = ScheduleJobMarshaller.builder().build();

    SchedulePlan schedulePlan = null;
    Optional<ScheduleJob> optJobSchedule = null;
    for (String[] parts :dataElements){
    	schedulePlan = schedulePlanService.getByCode(parts[SchedulingConstants.idx_SP_Code]).orElse(null);
      if (null==schedulePlan){
      	schedulePlan = schedulePlanMarshaller.marshal(parts);
        schedulePlanService.save(schedulePlan);
      }

      if (jobScheduleService.exists(GlobeConstants.PROP_CODE, parts[SchedulingConstants.idx_JobCode])){
        optJobSchedule = jobScheduleService.getByCode(parts[SchedulingConstants.idx_JobCode]);
        jobSchedule = optJobSchedule.get();
      } else {
        jobSchedule = scheduleJobMarshaller.marshal(parts);
        jobSchedule.setSchedulePlan(schedulePlan);

        jobScheduleService.save(jobSchedule);
      }

      if (null != jobSchedule){
        loadedJobSchedules.add(jobSchedule);
      }
    }
    return loadedJobSchedules;
  }

  private Trigger buildTrigger(String name, String group, LocalDateTime fireTime, String cron) {
    name = CommonUtility.isEmpty(name) ? java.util.UUID.randomUUID().toString() : name;
    if (!CommonUtility.isEmpty(cron)) {
      if (!CronExpression.isValidExpression(cron))
        throw new IllegalArgumentException("Provided expression " + cron + " is not a valid cron expression");

      return TriggerBuilder.newTrigger()
          .withIdentity(name, group)
          .withSchedule(CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionFireAndProceed().inTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault())))
          .usingJobData("cron", cron)
          .build();
    } else if (!CommonUtility.isEmpty(fireTime)) {
      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("fireTime", fireTime);
      return TriggerBuilder.newTrigger()
          .withIdentity(name, group)
          .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionNextWithExistingCount())
          .startAt(Date.from(fireTime.atZone(ZoneId.systemDefault()).toInstant()))
          .usingJobData(jobDataMap)
          .build();
    }
    throw new IllegalStateException("unsupported trigger descriptor " + this);
  }

  /*@Async
  public void initSchedulers(Context context) throws CerberusException {
    log.info("Enter initSchedulers()");

    List<ScheduleJob> jobSchedules = parseJobSchedules(context);
    if (CommonUtility.isEmpty(jobSchedules)){
      log.info("There is no job schedule to be run. ");
      return;
    }

    JobDetail currentJobDetail = null;
    Class<? extends Job> jobScheduleClass = null;
    Trigger currentTrigger = null;
    try {
      for (ScheduleJob jobSchedule :jobSchedules) {
        if (CommonUtility.isEmpty(jobSchedule.getJobClass()))
          continue;

        try {
          jobScheduleClass = (Class<? extends Job>)BeanUtility.getClass(jobSchedule.getJobClass());
        } catch (Exception e) {
          jobScheduleClass = null;
          log.error("An error occurred while get associated class from: " + jobSchedule.getJobClass());
        }

        if (null==jobScheduleClass)
          continue;

        currentTrigger = buildTrigger(jobSchedule.getName() + SchedulingConstants.specTrigger, jobSchedule.getCategory(), null, jobSchedule.getCronExpression());
        
        currentJobDetail = JobBuilder
            .newJob(jobScheduleClass)
            .withIdentity(jobSchedule.getName(), jobSchedule.getCategory())
            .build();

        scheduler.scheduleJob(currentJobDetail, currentTrigger);
      }

      if (!scheduler.isStarted()){
        scheduler.start();
      }
		} catch (Exception e) {
			throw new CerberusException(e);
		}
    log.info("Leave initSchedulers()");
  }*/

  @Async
  public void dispatch (List<String[]> dataElements) throws CerberusException {
    log.info("Enter initSchedulers()");

    List<ScheduleJob> jobSchedules = parseJobSchedules(dataElements);
    if (CommonUtility.isEmpty(jobSchedules)){
      log.info("There is no job schedule to be run. ");
      return;
    }

    JobDetail currentJobDetail = null;
    Class<? extends Job> jobScheduleClass = null;
    Trigger currentTrigger = null;
    try {
      for (ScheduleJob jobSchedule :jobSchedules) {
        if (CommonUtility.isEmpty(jobSchedule.getJobClass()))
          continue;

        try {
          jobScheduleClass = (Class<? extends Job>)BeanUtility.getClass(jobSchedule.getJobClass());
        } catch (Exception e) {
          jobScheduleClass = null;
          log.error("An error occurred while get associated class from: " + jobSchedule.getJobClass());
        }

        if (null==jobScheduleClass)
          continue;

        currentTrigger = buildTrigger(jobSchedule.getName() + SchedulingConstants.specTrigger, jobSchedule.getCategory(), null, jobSchedule.getCronExpression());
        
        currentJobDetail = JobBuilder
            .newJob(jobScheduleClass)
            .withIdentity(jobSchedule.getName(), jobSchedule.getCategory())
            .build();

        scheduler.scheduleJob(currentJobDetail, currentTrigger);
      }

      if (!scheduler.isStarted()){
        scheduler.start();
      }
		} catch (Exception e) {
			throw new CerberusException(e);
		}
    log.info("Leave initSchedulers()");
  }
}