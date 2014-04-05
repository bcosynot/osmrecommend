package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.NodeTag;
import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.repositories.NodeRepository;
import com.osmrecommend.persistence.repositories.NodeTagRepository;

@Service
@Configurable
public class NodePersistenceServiceImp implements NodeService {

	@Autowired
	NodeRepository repo;
	
	@Autowired
	NodeTagRepository tagRepo;
	
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
		
		for(Node n : repo.findAll()) {
			nodeIds.add(n.getId());
		}
		
		return nodeIds;
	}

	@Override
	public Map<String, String> getTagsForNode(Node node) {
		
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(node);
		
		Map<String, String> tags = new HashMap<String, String>();
		
		for(NodeTag nodeTag : tagRepo.findAll(nodes)) {
			
			tags.put(nodeTag.getK(), nodeTag.getV());
			
		}
		
		return tags;
		
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
	public Iterable<Node> getAllByUser(User user) {
		return repo.findByUser(user);
	}

	

}
