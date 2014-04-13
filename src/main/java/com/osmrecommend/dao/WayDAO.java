package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;

import org.grouplens.lenskit.data.dao.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.service.WayService;

@Component
public class WayDAO implements ItemDAO {

	@Autowired
	WayService service;
	
	@Override
	public LongSet getItemIds() {
		return service.getAllWayIDs();
	}
	
	public ObjectList<String> getItemTags(long item) {
		return service.getTagsForWayId(item);
	}
	
	public ObjectOpenHashBigSet<String> getTagVocabulary() {
		return new ObjectOpenHashBigSet<String>(service.getAllTags());
	}

}
