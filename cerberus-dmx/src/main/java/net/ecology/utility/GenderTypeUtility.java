/**
 * 
 */
package net.ecology.utility;

import java.util.List;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.domain.model.GenderType;

/**
 * @author bqduc
 *
 */
public class GenderTypeUtility {
	private static final List<String> femalePatterns = CollectionsUtility.arraysAsList(new String[] {"Nu", "Nữ", "Female", "Bà", "Cô"});
	private static final List<String> malePatterns = CollectionsUtility.arraysAsList(new String[] {"Nam", "Male", "Ong", "Chu", "Ông", "Chú"});

  public static GenderType getGenderType(String src){
  	if (CommonUtility.isEmpty(src))
  		return GenderType.Unknown;

  	if (femalePatterns.contains(src.toUpperCase()))
			return GenderType.Female;

		if (malePatterns.contains(src.toUpperCase()))
			return GenderType.Male;

		return GenderType.Unknown;
	}
}
