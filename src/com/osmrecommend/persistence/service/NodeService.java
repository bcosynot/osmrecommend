package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;

@Service
public interface NodeService {

	public Iterable<Node> getAllNodes();
	
	public Node getNodeById(Long id);
	
	public LongSet getAllNodeIDs();
	
	public Map<String, String> getTagsForNode(Node node);
	
	public LongSet getAllUserIds();
	
	public Iterable<Node> getAllByUser(User user);
}
