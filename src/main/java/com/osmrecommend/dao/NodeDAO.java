package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.HashSet;
import java.util.Set;

import org.grouplens.lenskit.data.dao.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.service.NodeService;

@Component
public class NodeDAO implements ItemDAO {

	@Autowired
	NodeService service;
	
	@Override
	public LongSet getItemIds() {
		return service.getAllNodeIDs();
	}
	
	public ObjectList<String> getItemTags(long item) {
		return service.getTagsForNodeId(item);
	}
	
	public Set<String> getTagVocabulary() {
		return new HashSet<String>(service.getAllTags());
	}

}
