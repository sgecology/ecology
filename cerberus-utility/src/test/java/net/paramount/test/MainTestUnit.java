/**
 * 
 */
package net.paramount.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 *
 */
public class MainTestUnit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testUndefiniteTime();
		//doTestLocale();
		//listLocales();
		collectionToString();
	}

	protected static void testUndefiniteTime() {
		Calendar decadeAfterCalendar = Calendar.getInstance();
		decadeAfterCalendar.add(Calendar.YEAR, 10);///10 years

		long startedTime = System.currentTimeMillis();
		
		System.out.println(decadeAfterCalendar.getTimeInMillis()-startedTime);
	}
	
	protected static void doTestLocale(){

		for (Locale locale :Locale.getAvailableLocales()){
			System.out.println(locale.getDisplayCountry()+ "|\t" + locale.getCountry()+ "|\t" + locale.getLanguage()+ "|\t"  + locale.getScript()+ "|\t" + locale.getVariant());
		}
	}

	protected static void listLocales() {
		int maxLanguage =0 ;
		int maxDisplayLanguage = 0;
		for (Locale locale :Locale.getAvailableLocales()) {
			maxLanguage = (maxLanguage > locale.getLanguage().length())?maxLanguage:locale.getLanguage().length();
			maxDisplayLanguage = (maxDisplayLanguage > locale.getDisplayLanguage().length())?maxDisplayLanguage:locale.getDisplayLanguage().length();
			System.out.println(locale.getCountry() + "\t\t" + locale.getLanguage() + "\t\t"+locale.getDisplayLanguage());
		}
		System.out.println(maxLanguage + "::" + maxDisplayLanguage);
	}

	protected static void collectionToString() {
		Collection<String> collection = new ArrayList<>();
		collection.add("One");collection.add("Two");collection.add("Three");collection.add("Four");
		System.out.println(String.join("|", Arrays.toString(collection.toArray())));
		System.out.println(String.join(GlobeConstants.PIPELINE, collection));
	}
}
