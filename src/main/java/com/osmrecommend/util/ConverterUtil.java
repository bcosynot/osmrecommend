package com.osmrecommend.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Map;
import java.util.Map.Entry;

public class ConverterUtil {

	/**
	 * @param object2ObjectOpenHashMap
	 */
	public static ObjectList<String> convertMapOfTagsToCombinedList(Object2ObjectOpenHashMap<String,String> object2ObjectOpenHashMap) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for (Entry<String, String> e : object2ObjectOpenHashMap.entrySet()) {

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
