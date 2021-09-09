/**
 * 
 */
package net.ecology.model;

/**
 * @author bqduc
 *
 */
public enum DateTimePatterns {
	ddMMyyyy_SLASH("dd/MM/yyyy"),
	ddMMyyyy_BAR("dd-MM-yyyy"),
	MMddyyyy_SLASH("MM/dd/yyyy"),
	MMddyyyy_BAR("MM-dd-yyyy"),
	yyyyMMdd_SLASH("yyyy/MM/dd"),
	yyyyMMdd_BAR("yyyy-MM-dd"),
	yyyyMMdd("yyyyMMdd"),
	ddMMyyyyHHmmss_UNDERSCORE("dd/MM/yyyy_HH:mm:ss"),
	ddMMyyyyHHmmss_SLASH("dd/MM/yyyy HH:mm:ss"),
	ddMyyyyhhmmss("dd-M-yyyy hh:mm:ss"), //02-1-2018 06:07:59
	ddMMMMyyyy("dd MMMM yyyy"),	//02 January 2018
	ddMMMMyyyyzzzz("dd MMMM yyyy zzzz"),	//02 January 2018 India Standard Time
	EddMMMyyyyHHmmssz("E, dd MMM yyyy HH:mm:ss z"),	//Tue, 02 Jan 2018 18:07:59 IST

	ddMMyy("dd-MM-yy"),
	yyyyMinusMMMinusdd("yyyy-MM-dd"),
	yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss"),
	yyyyMMddHHmmssSSS("yyyy-MM-dd HH:mm:ss.SSS"), 		
	yyyyMMddHHmmssSSSZ("yyyy-MM-dd HH:mm:ss.SSSZ"),	
	EEEEEMMMMMyyyyHHmmssSSSZ("EEEEE MMMMM yyyy HH:mm:ss.SSSZ")

	;


	private String dateTimePattern;

	private DateTimePatterns(String pattern){
		this.dateTimePattern = pattern;
	}

	public String getDateTimePattern() {
		return dateTimePattern;
	}
}
