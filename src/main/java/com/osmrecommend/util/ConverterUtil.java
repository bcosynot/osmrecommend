package com.osmrecommend.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Map;
import java.util.Map.Entry;

public class ConverterUtil {

	/**
	 * @param mapOfTags
	 */
	public static ObjectList<String> convertMapOfTagsToCombinedList(Object2ObjectMap<String, String> mapOfTags) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for (Entry<String, String> e : mapOfTags.entrySet()) {

			tags.add(e.getKey().toLowerCase() + e.getValue().toLowerCase());

		}
		
		return tags;
	}

	public static ObjectList<String> convertMapOfTagsToCombinedList(
			Map<String, String> tags) {
		return convertMapOfTagsToCombinedList((Object2ObjectMap<String, String>)tags);
	}
	
}
