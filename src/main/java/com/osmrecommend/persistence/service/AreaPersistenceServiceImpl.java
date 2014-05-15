/**
 * 
 */
package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Area;
import com.osmrecommend.persistence.repositories.AreaRepository;
import com.osmrecommend.persistence.repositories.AreaRepositoryCustom;
import com.osmrecommend.persistence.repositories.NodeRepository;
import com.osmrecommend.persistence.repositories.UserRepository;
import com.osmrecommend.persistence.repositories.WayRepository;

/**
 * @author Vivek
 *
 */
@Component
public class AreaPersistenceServiceImpl implements AreaService {

	@Autowired
	AreaRepository repo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	NodeRepository nodeRepo;
	
	@Autowired
	WayRepository wayRepo;
	
	@Override
	public LongSet getAllAreaIDs() {
		
		LongArraySet longSetOfIds = new LongArraySet();
		longSetOfIds.addAll(repo.fetchAllIds());
		
		return longSetOfIds;
	}

	@Override
	public Iterable<Area> getAllAreas() {
		return repo.findAll();
	}

	@Override
	public Area getAreaById(Long id) {
		return repo.findOne(id);
	}

	@Override
	public ObjectList<String> getTagsForAreaId(Long areaId) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		Area area = repo.findOne(areaId);
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findAllNodeTags(area.getTheGeom())) {
			
			tags.addAll(convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findAllWaysTags(area.getTheGeom())) {
			
			tags.addAll(convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
	}

	@Override
	public LongSet getAllUserIds() {
		
		LongSet userIds = new LongArraySet();

		userIds.addAll(userRepo.findAllUserIds());
		
		return userIds;
	}

	@Override
	public ObjectList<String> getAllTags() {

		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for(Object2ObjectMap<String, String> mapOfTags : nodeRepo.findAllTags()) {
			
			tags.addAll(convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		for(Object2ObjectMap<String, String> mapOfTags : wayRepo.findAllTags()) {
			
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
	
	@Autowired
	AreaRepositoryCustom customRepo;

	@Override
	public Long2ObjectMap<ObjectList<String>> getAllNodeTagsByArea() {
		return customRepo.fetchAllNodeTags();
	}

	@Override
	public Long2ObjectMap<ObjectList<String>> getAllWayTagsByArea() {
		return customRepo.fetchAllWayTags();
	}

	@Override
	public Long2LongMap findAllNodesByArea() {
		return customRepo.fetchAllNodes();
	}

	@Override
	public Long2LongMap findAllWaysByArea() {
		return customRepo.fetchAllWays();
	}

}
