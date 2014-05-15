package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;

@Service
public interface NodeService {

	public Iterable<Node> getAllNodes();
	
	public Node getNodeById(Long id);
	
	public LongSet getAllNodeIDs();
	
	public ObjectList<String> getTagsForNodeId(Long nodeId);
	
	public LongSet getAllUserIds();
	
	public Iterable<Node> getAllForUser(Long user);
	
	public Iterable<Node> getAllForUser(User user);
	
	public ObjectList<String> getAllTags();
}
