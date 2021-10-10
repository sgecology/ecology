/**
 * 
 */
package net.ecology.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import net.ecology.common.validator.DateValidator;
import net.ecology.model.CorpTimeZone;
import net.ecology.model.DateTimePatterns;

/**
 * @author bqduc
 *
 */
public final class DateTimeUtility {
	private static DateTimeUtility instance = new DateTimeUtility();
	private static final String DEFAULT_MINIMUM_TIME_PATTERN = "31/12/1970 13:01:01";
	private static final String DEFAULT_MAXIMUM_TIME_PATTERN = "31/12/2121 13:01:01";

	private static Calendar calendarInstance() {
		Calendar resp = new GregorianCalendar();
		return resp;
	}

	public static DateTimeUtility instance(){
		return instance;
	}

	public Date createDate(long longDateTime){
		return new Date(longDateTime);
	}

	public Date fillSystemTimeParts(long longDateTime){
		return this.fillSystemTimeParts(this.createDate(longDateTime));
	}

	public Date fillSystemTimeParts(Date date){
		Calendar fromCalendar = calendarInstance();
		fromCalendar.setTime(date);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, fromCalendar.get(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MONTH, fromCalendar.get(Calendar.MONTH));
		calendar.set(Calendar.YEAR, fromCalendar.get(Calendar.YEAR));

		return calendar.getTime();
	}

	public static Date systemTime(){
		return calendarInstance().getTime();
	}

	public static long getUtcTime(){
		Calendar utcCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		return utcCalendar.getTimeInMillis();
	}

	public static long getGmtTime(){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTimeInMillis();
	}

	public static Date add(Date date, int field, int amount){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	private static Date parseDateTime(String pattern, String value){
		Date retDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			retDate = simpleDateFormat.parse(value);
		} catch (ParseException e) {
			//log.error(e);
		}
		return retDate;
	}

	public static Date getMinimumTime(){
		return getMinimumTime(null);
	}

	public static Date getUnfinishedTime(){
		return getUnfinishedTime(null);
	}

	public static Date getMinimumTime(String configuredDefaultValue){
		if (CommonUtility.isEmpty(configuredDefaultValue)){
			return parseDateTime(DateTimePatterns.ddMMyyyyHHmmss_SLASH.getDateTimePattern(), DEFAULT_MINIMUM_TIME_PATTERN);
		}
		return parseDateTime(DateTimePatterns.ddMMyyyyHHmmss_SLASH.getDateTimePattern(), configuredDefaultValue);
	}

	public static Date getUnfinishedTime(String configuredDefaultValue){
		if (CommonUtility.isEmpty(configuredDefaultValue)){
			return parseDateTime(DateTimePatterns.ddMMyyyyHHmmss_SLASH.getDateTimePattern(), DEFAULT_MAXIMUM_TIME_PATTERN);
		}
		return parseDateTime(DateTimePatterns.ddMMyyyyHHmmss_SLASH.getDateTimePattern(), configuredDefaultValue);
	}

	public static Date systemDate() {
		Calendar sysCal = calendarInstance();
		sysCal.setTime(Calendar.getInstance().getTime());
		sysCal.set(Calendar.HOUR_OF_DAY, 0);
		sysCal.set(Calendar.MINUTE, 0);
		sysCal.set(Calendar.SECOND, 0);
		sysCal.set(Calendar.MILLISECOND, 0);
		return sysCal.getTime();
	}

	public static Date systemDateTime() {
		return systemTime();
	}

	public static String getSystemDateTimeString() {
		return dateToString(systemDateTime(), "dd/MM/yyyy HH:mm:ss");
	}

	public static String getSystemDateTimeString(String pattern) {
		if (CommonUtility.isEmpty(pattern))
			return getSystemDateTimeString();

		return dateToString(systemDateTime(), pattern);
	}

	public static String dateToString(Date dateValue, String pattern) {
		String stringRet = null;
		try {
			if (CommonUtility.isEmpty(dateValue))
				return null;

			if (CommonUtility.isEmpty(pattern)) {
				pattern = DateTimePatterns.ddMMyyyy_SLASH.getDateTimePattern();
			}

			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			stringRet = sdf.format(dateValue);
		} catch (Exception e) {
			//log.error(e);
		}
		return stringRet;
	}

	public static Date parseDate(String value, String pattern) {
		if (CommonUtility.isEmpty(value))
			return null;

		SimpleDateFormat sdf = null;
		Date ret = null;
		if (CommonUtility.isEmpty(pattern)) {
			for (int i = 0; i < DateTimePatterns.values().length; i++) {
				try {
					sdf = new SimpleDateFormat(DateTimePatterns.values()[i].getDateTimePattern());
					ret = sdf.parse(value);
				} catch (Exception e) {
					//log.info("Error while parsing date: " + value);
				}
				if (CommonUtility.isNotEmpty(ret))
					break;
			}
		} else {
			try {
				sdf = new SimpleDateFormat(pattern);
				ret = sdf.parse(value);
			} catch (Exception e) {
				//log.info("Error while parsing date: " + value);
			}
		}
		return ret;
	}

	public static Date parseDate(String data) {
		return parseDate(data, null);
	}

	public static Date createFreeDate(String value){
		Date freeDate = null;
		for (DateTimePatterns dateTimePattern :DateTimePatterns.values()){
			try {
				freeDate = parseDate(value, dateTimePattern.getDateTimePattern());
			} catch (Exception e) {
				freeDate = null;
			}

			if (null != freeDate)
				break;
		}
		return freeDate;
	}

	public static Date getDateInstance(String data, Date defaultValue) throws ParseException{
		DateValidator dateValidatorInstance = DateValidator.getInstance();
		if (!dateValidatorInstance.validate(data))
			return defaultValue;

		return dateValidatorInstance.getSimpleDateFormat().parse(data);
	}

	public static Date getDateInstance(String data) throws ParseException{
		DateValidator dateValidatorInstance = DateValidator.getInstance();
		if (!dateValidatorInstance.validate(data))
			return null;

		return dateValidatorInstance.getSimpleDateFormat().parse(data);
	}

	public static Date getDateInstance(String data, String[] patterns){
		Date parsedDate = null;
		for (String pattern :patterns){
			try {
				parsedDate = new SimpleDateFormat(pattern).parse(data);
			} catch (Exception e) {
				parsedDate = null;
			}

			if (null != parsedDate)
				return parsedDate;
		}
		throw new RuntimeException("The data[" + data + "] did not matches with any patterns " + patterns);
	}

	public static long generateTimeStamp(){
		return instance().calendarInstance().getTimeInMillis();
	}

	public static Date getDummyMaxDate() {
		return parseDate("31/12/2200");
	}

	public static List<CorpTimeZone> getTimezones(){
		TimeZone timeZone;
		List<CorpTimeZone> fetchedData = CollectionsUtility.newList();
		String[] availableZoneIDs = TimeZone.getAvailableIDs();
		for (String zoneId : availableZoneIDs) {
			timeZone = TimeZone.getTimeZone(zoneId); 
			fetchedData.add(CorpTimeZone.builder()
					.id(timeZone.getID())
					.displayName(timeZone.getDisplayName())
					.build()
					);
		}
		return fetchedData;
	}

	/*private static String displayTimeZone(TimeZone tz) {

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		String result = "";
		if (hours > 0) {
			result = String.format("(GMT+%d:%02d) %s %s", hours, minutes, tz.getID(), tz.getDisplayName(CommonUtility.LOCALE_VIETNAMESE));
		} else {
			result = String.format("(GMT%d:%02d) %s %s", hours, minutes, tz.getID(), tz.getDisplayName(CommonUtility.LOCALE_VIETNAMESE));
		}

		return result;
	}*/
}