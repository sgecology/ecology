/**
 * 
 */
package net.ecology.global;

/**
 * @author ducbq
 *
 */
public interface SchedulingConstants {
  static String CONFIG_SYSTEM_CONFIG = "systemConfig";
  static String CONFIG_SYSTEM_CONFIG_VALUE = "System Configuration";

  static String CONFIG_SCHEDULES = "schedules";
  static String CONFIG_SCHEDULES_VALUE = "Default schedules in the system";

  static String CTX_JOB_SCHEDULE_ELEMENTS = "ctxJobScheduleElements";
  static String CTX_SCHEDULE_PLANS = "ctxSchedulePlanElements";

  static String CTX_SEPARATOR = "; ";

  static String CTX_NAME = GlobeConstants.PROP_NAME;
  static String CTX_GROUP = "group";

  static String CTX_CLASS = "class";

  static String defaultGroup = "Aquarium";
  static String specJob = "Job";
  static String specTrigger = "Trigger";

  static short idx_SP_Code = 0;
  static short idx_SP_Name = 1;
  static short idx_SP_StartTime = 2;
  static short idx_SP_JobType = 3;
  static short idx_SP_Type = 4;
  static short idx_JobCrontime = 5;
  static short idx_JobClass = 6;
  static short idx_JobCode = 7;
  static short idx_JobName = 8;
  static short idx_JobDisplayName = 9;
  static short idx_JobCategory = 10;
}
