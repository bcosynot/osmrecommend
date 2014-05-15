/**
 * 
 */
package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Area;

/**
 * @author Vivek
 *
 */
@Service
public interface AreaService {

	public LongSet getAllAreaIDs();
	
	public Iterable<Area> getAllAreas();
	
	public Area getAreaById(Long id);
	
	public ObjectList<String> getTagsForAreaId(Long areaId);
	
	public LongSet getAllUserIds();
	
	public ObjectList<String> getAllTags();
	
	public Long2LongMap findAllNodesByArea();
	
	public Long2ObjectMap<ObjectList<String>> getAllNodeTagsByArea();
	
	public Long2LongMap findAllWaysByArea();
	
	public Long2ObjectMap<ObjectList<String>> getAllWayTagsByArea();
	
}
