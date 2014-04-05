package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.domain.WayTag;
import com.osmrecommend.persistence.repositories.WayRepository;
import com.osmrecommend.persistence.repositories.WayTagRepository;

@Component
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
	public Object2ObjectMap<String, String> getTagsForNode(Way node) {

		ObjectList<Way> ways = new ObjectArrayList<Way>();
		ways.add(node);
		
		Object2ObjectMap<String, String> tags = new Object2ObjectOpenHashMap<String, String>();
		
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
