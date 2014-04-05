package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.NodeTag;
import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.repositories.NodeRepository;
import com.osmrecommend.persistence.repositories.NodeTagRepository;

@Component
public class NodePersistenceServiceImpl implements NodeService {

	
	private static Logger log = LoggerFactory.getLogger(NodePersistenceServiceImpl.class);
	
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

	@Override
	public Map<String, String> getTagsForNodeId(Long nodeId) {
		return getTagsForNode(repo.findOne(nodeId));
	}

	@Override
	public Map<String, String> getAllTags() {
		
		if(null == tagRepo) {
			log.info("tagRepo is null");
		} else {
			log.info("tagrepo isn't null");
		}
		Map<String, String> tags = new HashMap<String, String>();
		
		for(NodeTag nodeTag : tagRepo.findAll()) {
			
			tags.put(nodeTag.getK(), nodeTag.getV());
			
		}
		
		return tags;
	}

	

}
