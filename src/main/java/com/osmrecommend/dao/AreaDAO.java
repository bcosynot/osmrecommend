package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import org.grouplens.lenskit.collections.LongUtils;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Area;
import com.osmrecommend.persistence.service.AreaService;

@Component
public class AreaDAO implements ItemDAO {

	public static final Logger logger = LoggerFactory.getLogger(AreaDAO.class);

	@Autowired
	AreaService service;

	Long2ObjectMap<Area> allAreas;

	@Override
	public LongSet getItemIds() {
		if (null == allAreas)
			return service.getAllAreaIDs();
		else {
			return LongUtils.packedSet(allAreas.keySet());
		}
	}

	public ObjectList<String> getItemTags(Long id) {
		return service.getTagsForAreaId(id);
	}

	public ObjectSet<String> getTagVocabulary() {

		ObjectSet<String> setOfTags = new ObjectArraySet<String>();
		setOfTags.addAll(service.getAllTags());

		return setOfTags;
	}

	public Long2ObjectMap<Area> getAllAreasMap(Long2ObjectMap<Area> allAreasMap) {

		if(null == allAreasMap) {
			allAreasMap = new Long2ObjectOpenHashMap<Area>();
		}
		
		if (null == this.allAreas) {
			for (Area area : service.getAllAreas()) {
				allAreasMap.put(area.getId(), area);
			}
			this.allAreas = allAreasMap;
		}

		return allAreasMap;

	}

	public Long2ObjectMap<Area> getAllAreasMap() {
		Long2ObjectMap<Area> allAreasMap = new Long2ObjectOpenHashMap<Area>();
		return getAllAreasMap(allAreasMap);
	}

	public Long2LongMap getNodesByArea(Long2LongMap nodesByArea) {
		logger.info("fetching all nodes by area");
		return (nodesByArea = service.findAllNodesByArea());
	}

	public Long2LongMap getNodesByArea() {
		logger.info("fetching all nodes by area");
		return service.findAllNodesByArea();
	}

	public Long2ObjectMap<ObjectList<String>> getAllNodeTagsByArea(
			Long2ObjectMap<ObjectList<String>> allNodeTagsByArea) {
		logger.info("fetching all node tags by area");
		return (allNodeTagsByArea = service.getAllNodeTagsByArea());
	}

	public Long2ObjectMap<ObjectList<String>> getAllNodeTagsByArea() {
		logger.info("fetching all node tags by area");
		return service.getAllNodeTagsByArea();
	}

	public Long2LongMap getWaysByArea() {
		logger.info("fetching all ways by area");
		return service.findAllWaysByArea();
	}

	public Long2ObjectMap<ObjectList<String>> getAllWayTagsByArea(
			Long2ObjectMap<ObjectList<String>> allWayTagsByArea) {
		logger.info("fetching all way tags by area");
		return (allWayTagsByArea = service.getAllWayTagsByArea());
	}

	public Long2ObjectMap<ObjectList<String>> getAllWayTagsByArea() {
		logger.info("fetching all way tags by area");
		return service.getAllWayTagsByArea();
	}

}
