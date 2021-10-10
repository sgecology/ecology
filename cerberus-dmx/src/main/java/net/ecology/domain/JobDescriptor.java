/**
 * 
 */
package net.ecology.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.ecology.base.JobSchedulerBase;
import net.ecology.global.SchedulingConstants;
import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;

/**
 * @author ducbq
 *
 */
@Setter
@Getter
@Builder
public class JobDescriptor extends CoreObject {

  /**
   * 
   */
  private static final long serialVersionUID = 2345318563096720967L;

  // TODO add boolean fields for HTML and Attachments
  @NotBlank
  private String name;
  private String group;
  @NotEmpty
  private String subject;
  @NotEmpty
  private String messageBody;
  @NotEmpty
  private List<String> to;
  private List<String> cc;
  private List<String> bcc;
  private Map<String, Object> data = new LinkedHashMap<>();
  @JsonProperty("triggers")
  private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();
  private JobDetail jobDetail;

  public JobDescriptor setName(final String name) {
    this.name = name;
    return this;
  }

  public JobDescriptor setGroup(final String group) {
    this.group = group;
    return this;
  }

  public JobDescriptor setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public JobDescriptor setMessageBody(String messageBody) {
    this.messageBody = messageBody;
    return this;
  }

  public JobDescriptor setTo(List<String> to) {
    this.to = to;
    return this;
  }

  public JobDescriptor setCc(List<String> cc) {
    this.cc = cc;
    return this;
  }

  public JobDescriptor setBcc(List<String> bcc) {
    this.bcc = bcc;
    return this;
  }

  public JobDescriptor setData(final Map<String, Object> data) {
    this.data = data;
    return this;
  }

  public JobDescriptor setTriggerDescriptors(final List<TriggerDescriptor> triggerDescriptors) {
    this.triggerDescriptors = triggerDescriptors;
    return this;
  }

  /**
   * Convenience method for building Triggers of Job
   * 
   * @return Triggers for this JobDetail
   */
  @JsonIgnore
  public Set<Trigger> buildTriggers() {
    Set<Trigger> triggers = new LinkedHashSet<>();
    for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
      triggers.add(triggerDescriptor.buildTrigger());
    }

    return triggers;
  }

  /**
   * Convenience method that builds a JobDetail
   * 
   * @return the JobDetail built from this descriptor
   */
  public JobDetail buildJobDetail(JobSchedulerBase jobScheduler) {
    // @formatter:off
    JobDataMap jobDataMap = new JobDataMap(getData());
    jobDataMap.put("subject", subject);
    jobDataMap.put("messageBody", messageBody);
    jobDataMap.put("to", to);
    jobDataMap.put("cc", cc);
    jobDataMap.put("bcc", bcc);
    return JobBuilder.newJob(jobScheduler.getClass()).withIdentity(getName(), getGroup()).usingJobData(jobDataMap).build();
    // @formatter:on
  }

  /**
   * Convenience method that builds a JobDetail
   * 
   * @return the JobDetail built from this descriptor
   */
  public static JobDetail buildJobDetail(Context context) {
    // @formatter:off
    JobDataMap jobDataMap = new JobDataMap();
    context.getValues().forEach((key, value) -> {
      if (!SchedulingConstants.CTX_NAME.equalsIgnoreCase((String)key) && 
      		!SchedulingConstants.CTX_GROUP.equalsIgnoreCase((String)key) && 
      		!SchedulingConstants.CTX_CLASS.equalsIgnoreCase((String)key)) {
        jobDataMap.put((String)key, value);
      }
    });

    return JobBuilder.newJob((Class<? extends Job>)context.get(SchedulingConstants.CTX_CLASS))
        .withIdentity((String)context.get(SchedulingConstants.CTX_NAME), (String)context.get(SchedulingConstants.CTX_GROUP))
        .usingJobData(jobDataMap)
        .build();
    // @formatter:on
  }

  /**
   * Convenience method that builds a JobDetail
   * 
   * @return the JobDetail built from this descriptor
   */
  public static JobDetail buildJobDetail(String group, String name, Class<? extends Job> jobClass) {
    return JobBuilder.newJob(jobClass)
        .withIdentity(name, group)
        .build();
  }

  /**
   * Convenience method that builds a descriptor from JobDetail and Trigger(s)
   * 
   * @param jobDetail
   *          the JobDetail instance
   * @param triggersOfJob
   *          the Trigger(s) to associate with the Job
   * @return the JobDescriptor
   */
  @SuppressWarnings("unchecked")
  public static JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
    // @formatter:off
    List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

    for (Trigger trigger : triggersOfJob) {
      triggerDescriptors.add(TriggerDescriptor.buildDescriptor(trigger));
    }

    JobDescriptor jobDescriptor = JobDescriptor.builder()
        .name(jobDetail.getKey().getName())
        .group(jobDetail.getKey().getGroup())
        .subject(jobDetail.getJobDataMap().getString("subject"))
        .messageBody(jobDetail.getJobDataMap().getString("messageBody"))
        .to((List<String>) jobDetail.getJobDataMap().get("to"))
        .cc((List<String>) jobDetail.getJobDataMap().get("cc"))
        .bcc((List<String>) jobDetail.getJobDataMap().get("bcc"))
        // .setData(jobDetail.getJobDataMap().getWrappedMap())
        .triggerDescriptors(triggerDescriptors)
        .jobDetail(jobDetail)
        .build()
        ;
    jobDescriptor.build();
    return jobDescriptor;
    // @formatter:on
  }
  
  public JobDescriptor build(){
    StringBuilder readableCronExp = new StringBuilder();
    Options options = new Options();
    options.setZeroBasedDayOfWeek(false);
    for (TriggerDescriptor triggerDescriptor :this.triggerDescriptors){
      try {
        readableCronExp
        .append(CronExpressionDescriptor.getDescription(triggerDescriptor.getCronExpression(), options))
        .append("; ");
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    this.subject = readableCronExp.substring(0, readableCronExp.lastIndexOf(SchedulingConstants.CTX_SEPARATOR));
    return this;
  }
}
