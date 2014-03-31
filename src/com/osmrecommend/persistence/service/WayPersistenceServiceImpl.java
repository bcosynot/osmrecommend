package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.domain.WayTag;
import com.osmrecommend.persistence.repositories.WayRepository;
import com.osmrecommend.persistence.repositories.WayTagRepository;

public class WayPersistenceServiceImpl implements WayService {

	@Autowired
	WayRepository repo;
	
	@Autowired
	WayTagRepository tagRepo;
	
	@Override
	public Iterable<Way> getAllWays() {
		return repo.findAll();
	}

	@Override
	public Way getWayById(Long id) {
		return repo.findOne(id);
	}

	@Override
	public Iterable<Long> getAllWayIDs() {

		LongSet wayIds = new LongArraySet();
		
		for(Way way : repo.findAll()) {
			wayIds.add(way.getId());
		}
		
		return wayIds;
		
	}

	@Override
	public Map<String, String> getTagsForNode(Way node) {

		List<Way> ways = new ArrayList<Way>();
		ways.add(node);
		
		Map<String, String> tags = new HashMap<String, String>();
		
		for(WayTag wayTag : tagRepo.findAll(ways)) {
			
			tags.put(wayTag.getK(), wayTag.getV());
			
		}
		
		return tags;
		
	}

	@Override
	public LongSet getAllUserIds() {

		LongSet userIds = new LongArraySet();
		
		for(Way way : repo.findAll()) {
			
			userIds.add(way.getUser().getId());
			
		}
		
		return userIds;
		
	}

}
