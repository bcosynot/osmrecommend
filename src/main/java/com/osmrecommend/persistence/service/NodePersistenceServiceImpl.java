package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.repositories.NodeRepository;
import com.osmrecommend.persistence.repositories.UserRepository;
import com.osmrecommend.util.ConverterUtil;

@Component
public class NodePersistenceServiceImpl implements NodeService {

	@Autowired
	NodeRepository repo;
	
	@Autowired
	UserRepository userRepo;
	
	@Override
	public Iterable<Node> getAllNodes() {
		return repo.findAll();
	}

	@Override
	public Node getNodeById(Long id) {
		return repo.findOne(id);
	}

	@Override
	public LongSet getAllNodeIDs() {
		
		LongSet nodeIds = new LongArraySet();
		
		for(Long nodeId : repo.findAllNodeIds()) {
			nodeIds.add(nodeId);
		}
		
		return nodeIds;
	}

	@Override
	public LongSet getAllUserIds() {
		
		LongSet userIds = new LongArraySet();
		
		for(Node node : repo.findAll()) {
			
			userIds.add(node.getUser().getId());
			
		}
		
		return userIds;
		
	}
	
	@Override
	public Iterable<Node> getAllForUser(Long userId) {
		User user = userRepo.findOne(userId);
		return getAllForUser(user);
	}

	@Override
	public Iterable<Node> getAllForUser(User user) {
		return repo.findAllNodesForUser(user);
	}

	@Override
	public ObjectList<String> getTagsForNodeId(Long nodeId) {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findTagsByNodeId(nodeId)) {
			
			tags.addAll(ConverterUtil.convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
	}

	@Override
	public ObjectList<String> getAllTags() {
		
		ObjectList<String> tags = new ObjectArrayList<String>();
		
		for(Object2ObjectMap<String, String> mapOfTags : repo.findAllTags()) {
			
			tags.addAll(ConverterUtil.convertMapOfTagsToCombinedList(mapOfTags));
			
		}
		
		return tags;
		
	}


}
