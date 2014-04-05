package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;

@Service
public interface NodeService {

	public Iterable<Node> getAllNodes();
	
	public Node getNodeById(Long id);
	
	public LongSet getAllNodeIDs();
	
	public Object2ObjectMap<String, String> getTagsForNode(Node node);
	
	public Object2ObjectMap<String, String> getTagsForNodeId(Long nodeId);
	
	public LongSet getAllUserIds();
	
	public Iterable<Node> getAllByUser(User user);
	
	public Object2ObjectMap<String, String> getAllTags();
}
