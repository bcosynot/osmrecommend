package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;

public interface AreaRepositoryCustom {

	public Long2LongMap fetchAllNodes();
	
	public Long2ObjectMap<ObjectList<String>> fetchAllNodeTags();
	
	public Long2LongMap fetchAllWays();
	
	public Long2ObjectMap<ObjectList<String>> fetchAllWayTags();
	
}
