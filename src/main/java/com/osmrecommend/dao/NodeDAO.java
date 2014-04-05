package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.grouplens.lenskit.data.dao.ItemDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.service.NodeService;

@Component
public class NodeDAO implements ItemDAO {

	private static Logger log = LoggerFactory.getLogger(NodeDAO.class);
	
	@Autowired
	NodeService service;
	
	@Override
	public LongSet getItemIds() {
		return service.getAllNodeIDs();
	}
	
	public List<String> getItemTags(long item) {
		
		List<String> itemTags = new ArrayList<String>();
		
		Map<String, String> mapOfItemTags = service.getTagsForNodeId(item);
		
		for(Entry<String, String> entry : mapOfItemTags.entrySet()) {
		
			itemTags.add(entry.getKey()+entry.getValue());
			
		}
		
		return itemTags;
	}
	
	public Set<String> getTagVocabulary() {
		
		Set<String> tagVocabulary = new HashSet<String>();
		
		Map<String, String> mapOfItemTags = null;
		
		if(null == service) {
			log.info("service is null.");
		} else {
			log.info("service isn't null.");
		}
		
		if(null == (mapOfItemTags = service.getAllTags())) {
			log.info("allags is null");
		} else {
			log.info("alltags isn't null.");
		}
				
		for(Entry<String, String> entry : mapOfItemTags.entrySet()) {
			
			tagVocabulary.add(entry.getKey()+entry.getValue());
			
		}
		
		return tagVocabulary;
	}
	
	/*public NodeDAO() {
		
		service = new NodePersistenceServiceImpl();
		
	}*/

}
