package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import org.grouplens.lenskit.data.dao.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.service.AreaService;

@Component
public class AreaDAO implements ItemDAO {

	@Autowired
	AreaService service;
	
	@Override
	public LongSet getItemIds() {
		return service.getAllAreaIDs();
	}
	
	public ObjectList<String> getItemTags(Long id) {
		return service.getTagsForAreaId(id);
	}
	
	public ObjectSet<String> getTagVocabulary() {
		
		ObjectSet<String> setOfTags = new ObjectArraySet<String>();
		setOfTags.addAll(service.getAllTags());
		
		return setOfTags;
	}

}
