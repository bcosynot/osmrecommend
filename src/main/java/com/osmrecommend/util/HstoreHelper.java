package com.osmrecommend.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.util.StringUtils;

// courtesy of: http://backtothefront.net/2011/storing-sets-keyvalue-pairs-single-db-column-hibernate-postgresql-hstore-type/
public class HstoreHelper {

	private static final String K_V_SEPARATOR = "=>";

	public static String toString(Object2ObjectOpenHashMap<String, String> m) {
		if (m.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int n = m.size();
		for (String key : m.keySet()) {
			sb.append("\"" + key + "\"" + K_V_SEPARATOR + "\"" + m.get(key)
					+ "\"");
			if (n > 1) {
				sb.append(", ");
				n--;
			}
		}
		return sb.toString();
	}

	public static Object2ObjectOpenHashMap<String, String> toMap(String s) {
		Object2ObjectOpenHashMap<String, String> m = new Object2ObjectOpenHashMap<String, String>();
		if (!StringUtils.hasText(s)) {
			return m;
		}
		String[] tokens = s.split(", ");
		for (String token : tokens) {
			String[] kv = token.split(K_V_SEPARATOR);
			String k = kv[0];
			k = k.trim().substring(1, k.length() - 1);
			String v = "";
			if (kv.length > 1) {
				v = kv[1];
				v = v.trim().substring(1, v.length() - 1);
			}
			m.put(k, v);
		}
		return m;
	}

	public static ObjectList<String> toMapValues(String s) {

		ObjectList<String> values = new ObjectArrayList<String>();
		
		if (!StringUtils.hasText(s)) {
			return values;
		}
		String[] tokens = s.split(", ");
		for (String token : tokens) {
			String[] kv = token.split(K_V_SEPARATOR);
			String k = kv[0];
			k = k.trim().substring(1, k.length() - 1);
			String v = "";
			if (kv.length > 1) {
				v = kv[1];
				v = v.trim().substring(1, v.length() - 1);
			}
			values.add(v);
		}
		
		return values;
		
	}

}