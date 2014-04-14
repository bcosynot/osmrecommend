package com.osmrecommend.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConverterUtil {

	/**
	 * @param hashMap
	 */
	public static ObjectList<String> convertMapOfTagsToCombinedList(HashMap<String,Object> hashMap) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for (Entry<String, Object> e : hashMap.entrySet()) {

			String value = (String) e.getValue();
			tags.add(e.getKey().toLowerCase() + value.toLowerCase());

		}
		
		return tags;
	}

	public static ObjectList<String> convertMapOfTagsToCombinedList(
			Map<String, String> tags) {
		return convertMapOfTagsToCombinedList((Object2ObjectMap<String, String>)tags);
	}
	
}
