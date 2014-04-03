package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Way;

@Service
public interface WayService {

public Iterable<Way> getAllWays();
	
	public Way getWayById(Long id);
	
	public Iterable<Long> getAllWayIDs();
	
	public Map<String, String> getTagsForNode(Way node);
	
	public LongSet getAllUserIds();
	
}
