package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.repositories.WayRepository;
import com.osmrecommend.util.ConverterUtil;

@Component
public class WayPersistenceServiceImpl implements WayService {

	private static final Logger logger = LoggerFactory.getLogger(WayPersistenceServiceImpl.class);
	
	@Autowired
	WayRepository repo;
	
	@Override
	public Iterable<Way> getAllWays() {
		
		logger.info("Inside getAllWays");
		
		Long ts = System.currentTimeMillis();
		Iterable<Way> allWays = repo.findAll();
		logger.info("All ways fetched in " + (System.currentTimeMillis() - ts)/1000 + "s");
		
		return allWays;
		
	}
	
	@Override
	public Iterable<Way> getSomeWays(int limit) {
		
		logger.info("Inside getSomeWays");
		
		Pageable page = new PageRequest(0, limit);
		Long ts = System.currentTimeMillis();
		Iterable<Way> someWays = repo.findSome(page);
		logger.info("Some ways fetched in " + (System.currentTimeMillis() - ts)/1000 + "s");
		
		return someWays;
		
	}

	@Override
	public Way getWayById(Long id) {
		
		logger.info("Inside getWayById");
		return repo.findOne(id);
	}

	@Override
	public LongSet getAllWayIDs() {
		
		logger.info("Inside getAllWayIDs");

		LongSet wayIds = new LongArraySet();
		
		for(Long wayId : repo.findAllWayIds()) {
			wayIds.add(wayId);
		}
		
		return wayIds;
		
	}

	@Override
	public LongSet getAllUserIds() {
		
		logger.info("Inside getAllUserIds");

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
			
			tags.addAll(ConverterUtil.convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
		
	}

	@Override
	public ObjectList<String> getAllTags() {
		
		logger.info("Inside getAllTags");

		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findAllTags()) {
			
			tags.addAll(ConverterUtil.convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
		
	}

	@Override
	public Iterable<Way> getAllForUser(User user) {
		return repo.findByUser(user);
	}
	
}
