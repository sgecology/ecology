/**
 * 
 */
package net.ecology.utility;

import java.text.ParseException;
import java.util.Locale;

import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;

/**
 * @author ducbq
 *
 */
public class SchedulerUtility {
  public static String parseCronExpressionReadable(String cronExpression, Locale locale) throws ParseException{
    Options options = new Options();
    options.setZeroBasedDayOfWeek(false);
    return CronExpressionDescriptor.getDescription(cronExpression, options, locale);
  }
}
