package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;

@Repository
public interface NodeRepository extends PagingAndSortingRepository<Node, Long> {

	public Iterable<Node> findByUser(User user);
	
	@Query("SELECT n.tags FROM Node n")
	public Iterable<Object2ObjectMap<String, String>> findAllTags();
	
	@Query("SELECT n.tags FROM Node n WHERE n.nodeId = :nodeId")
	public Iterable<Object2ObjectMap<String, String>> findTagsByNodeId(@Param("nodeId") Long nodeId);
	
	public Iterable<Node> findByNodeId(Long nodeId);
	
	@Query("SELECT n.nodeId FROM Node n")
	public Iterable<Long> findAllNodeIds();
	
}
