/**
 * 
 */
package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Area;
import com.osmrecommend.persistence.domain.User;

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
	
	public Iterable<Area> getAllByUser(User user);
	
	public ObjectList<String> getAllTags();
	
}
