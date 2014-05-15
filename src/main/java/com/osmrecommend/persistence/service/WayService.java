package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.domain.Way;

@Service
public interface WayService {

	public Iterable<Way> getAllWays();
	
	public Iterable<Way> getSomeWays(int limit);
	
	public Iterable<Way> getAllForUser(User user);
	
	public Way getWayById(Long id);
	
	public LongSet getAllWayIDs();
	
	public ObjectList<String> getTagsForWayId(Long wayId);
	
	public ObjectList<String> getAllTags();
	
	public LongSet getAllUserIds();
	
}
