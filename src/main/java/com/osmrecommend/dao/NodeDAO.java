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

import com.osmrecommend.persistence.service.NodeService;

@Component
public class NodeDAO implements ItemDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(NodeDAO.class);

	@Autowired
	NodeService service;
	
	@Override
	public LongSet getItemIds() {
		return service.getAllNodeIDs();
	}
	
	public ObjectList<String> getItemTags(long item) {
		return service.getTagsForNodeId(item);
	}
	
	public ObjectOpenHashBigSet<String> getTagVocabulary() {

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
			logger.info("tempAllTags isn't null");
			allTags.addAll(tempAllTags);
		}
		
		return new ObjectOpenHashBigSet<String>(allTags);
		
	}

}
