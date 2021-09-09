/**
 * 
 */
package net.ecology.helper;

import java.util.HashMap;
import java.util.Map;

import net.ecology.global.GlobeConstants;

/**
 * @author bqduc
 *
 */
public class WebServicingHelper {
	public static Map<String, Object> createSearchParameters(String keyword, Short page, Short pageSize) {
		Map<String, Object> defaultSearchParameters = new HashMap<>();
		defaultSearchParameters.put(GlobeConstants.PARAM_KEYWORD, keyword);
		defaultSearchParameters.put(GlobeConstants.PARAM_PAGE, (null != page)?page:1);
		defaultSearchParameters.put(GlobeConstants.PARAM_PAGE_SIZE, (null != pageSize)?pageSize:GlobeConstants.DEFAULT_PAGE_SIZE);
		return defaultSearchParameters;
	}

}
