package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Way;
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
	public LongSet getAllWayIDs() {

		LongSet wayIds = new LongArraySet();
		
		for(Way way : repo.findAll()) {
			wayIds.add(way.getId());
		}
		
		return wayIds;
		
	}

	@Override
	public LongSet getAllUserIds() {

		LongSet userIds = new LongArraySet();
		
		for(Way way : repo.findAll()) {
			
			userIds.add(way.getUser().getId());
			
		}
		
		return userIds;
		
	}

	@Override
	public ObjectList<String> getTagsForWayId(Long wayId) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findTagsByWayId(wayId)) {
			
			tags.addAll(convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
	}

	@Override
	public ObjectList<String> getAllTags() {

		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findAllTags()) {
			
			tags.addAll(convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
		
	}
	
	/**
	 * @param mapOfTags
	 */
	private ObjectList<String> convertMapOfTagsToCombinedList(Object2ObjectMap<String, String> mapOfTags) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for (Entry<String, String> e : mapOfTags.entrySet()) {

			tags.add(e.getKey().toLowerCase() + e.getValue().toLowerCase());

		}
		
		return tags;
	}

}
