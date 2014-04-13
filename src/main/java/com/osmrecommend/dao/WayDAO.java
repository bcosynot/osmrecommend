package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;

import org.grouplens.lenskit.data.dao.ItemDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.service.WayService;

@Component
public class WayDAO implements ItemDAO {

	private static final Logger logger = LoggerFactory.getLogger(WayDAO.class);
	
	@Autowired
	WayService service;
	
	@Override
	public LongSet getItemIds() {
		logger.info("Fetching all Way IDs");
		return service.getAllWayIDs();
	}
	
	public ObjectList<String> getItemTags(long item) {
		return service.getTagsForWayId(item);
	}
	
	public ObjectList<Way> getAllWays() {
		logger.info("Fetching all Ways");
		Iterable<Way> allWays = service.getAllWays();
		ObjectList<Way> allWaysList = new ObjectArrayList<Way>();
		for(Way way : allWays) {
			allWaysList.add(way);
		}
		return allWaysList;
	}
	
	public ObjectOpenHashBigSet<String> getTagVocabulary() {
		
		logger.info("fetching tag vocabulary");
		if(null==service) {
			logger.info("service is null");
		} else {
			logger.info("service isn't null");
		}
		
		ObjectList<String> allTags = new ObjectArrayList<String>();
		ObjectList<String> tempAllTags;
		if(null == (tempAllTags = service.getAllTags())) {
			logger.info("tempAllTags is null");
		} else {
			logger.info("tempAllTags isn't null. size:"+tempAllTags.size());
			allTags.addAll(tempAllTags);
		}
		
		return new ObjectOpenHashBigSet<String>(allTags);
		
	}

}
