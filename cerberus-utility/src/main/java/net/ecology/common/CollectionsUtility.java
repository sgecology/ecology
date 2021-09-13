/**
 * 
 */
package net.ecology.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ecology.global.GlobeConstants;
import net.ecology.model.ui.UISelectItem;

/**
 * @author bqduc
 *
 */
public class CollectionsUtility {
	@SafeVarargs
	public static <T> List<T> newList(T ...initialValues){
		List<T> dataList = new ArrayList<T>();
		for (T object :initialValues) {
			dataList.add(object);
		}
		return dataList;
	}

	public static <T> Collection<T> newCollection(){
		return newList();
	}

	public static <T> Collection<T> newList(){
		return createArrayList();
	}

	private static <T> List<T> createArrayList(){
		return new ArrayList<T>();
	}

	public static <K, T> HashMap <K, T> newMap(){
		return new HashMap<>();
	}

	public static <T> T[] listToArray(List<T> objects) {
		if (CommonUtility.isEmpty(objects))
			return null;

		return (T[])objects.toArray();
	}

	public static <K, T> Map <K, T> newHashedMap(Object ...keyValuePairs){
		Map<K, T> retMap = new HashMap<>();
		for (int i = 0; i < keyValuePairs.length; i+= 2) {
			K key = (K)keyValuePairs[i];
			T value = (T)keyValuePairs[i+1];
			retMap.put(key, value);
		}

		return retMap;
	}

	public static <T> HashSet <T> newHashSet(){
		return new HashSet<>();
	}

	public static <T> List<T> arraysAsList(T [] objects){
		return Arrays.asList(objects);
	}

	public static <T> void cleanEmtpty(List<T> objects){
		Object objectChecker = objects.get(0);
		if (!(objectChecker instanceof List && ((List<?>) objectChecker).get(0) instanceof String)){
			return;
		}

		int emptyCounter = 0;
		List<?> checkerObjects = null;
		List<T> cleaned = createArrayList();
		for (T t :objects){
			if (CommonUtility.isEmpty(t))
				continue;

			emptyCounter = 0;
			checkerObjects = (List<?>)t;
			for (Object object :checkerObjects){
				if (CommonUtility.isEmpty(object))
					++emptyCounter;
			}

			if (emptyCounter >= checkerObjects.size())
				continue;

			cleaned.add(t);
		}
		objects.clear();
		objects.addAll(cleaned);
	}

	public static <T> boolean isEmptyList(List<T> list){
		if (CommonUtility.isEmpty(list))
			return true;

		int emptyElements = 0;
		for (T object :list){
			if (CommonUtility.isEmpty(object))
				++emptyElements;
		}

		return (emptyElements >= list.size());
	}

	public static <T> boolean isNotEmptyList(List<T> list){
		return (false==isEmptyList(list));
	}

	public static <T> T getEntry(List<T> list, int index){
		T result = null;
		try {
			result = list.get(index);
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	public static <T> Set<T> asSet(Collection<T> objects){
		Set<T> results = new HashSet<>();
		results.addAll(objects);
		return results;
	}

	public static List<UISelectItem> createSelectItems(List<?> businessObjects, Map<String, String> mappedAttributes) {
		List<UISelectItem> suggestedItems = newList();
		Map<String, Object> objectAttributeMap = null;
		for (Object businessObject : businessObjects) {
			try {
				objectAttributeMap = BeanUtility.getObjectAttributes(businessObject, asSet(mappedAttributes.values()));
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				e.printStackTrace();
				//log.error(e.getMessage(), e); 
			}

			if (CommonUtility.isNotEmpty(objectAttributeMap)){
				suggestedItems.add(UISelectItem.builder()
						.id((Long)objectAttributeMap.get(mappedAttributes.get(GlobeConstants.PROP_ID)))
						.code((String)objectAttributeMap.get(mappedAttributes.get(GlobeConstants.PROP_CODE)))
						.name((String)objectAttributeMap.get(mappedAttributes.get(GlobeConstants.PROP_NAME)))
						.nameLocal((String)objectAttributeMap.get(mappedAttributes.get(GlobeConstants.PROPERTY_NAME_LOCAL)))
						.build());
			}
		}
		return suggestedItems;
	}

	public static <K, T> HashMap <K, T> cloneMap(Map<K, T> srcMap){
		HashMap <K, T> clonedMap = newMap();
		clonedMap.putAll(srcMap);
		return clonedMap;
	}
}
