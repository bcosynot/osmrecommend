package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.domain.Way;

@Repository
public interface WayRepository extends PagingAndSortingRepository<Way, Long> {

	public Iterable<Way> findByUser(User user);
	
	@Query("SELECT w FROM Way w")
	public List<Way> findSome(Pageable pageable);
	
	@Query("SELECT w.tags FROM Way w")
	public Iterable<Object2ObjectMap<String, String>> findAllTags();
	
	@Query("SELECT w.tags FROM Way w WHERE w.wayId = :wayId")
	public Iterable<Object2ObjectMap<String, String>> findTagsByWayId(@Param("wayId") Long wayId);
	
	public Iterable<Node> findByWayId(Long wayId);
	
	@Query("SELECT w.wayId FROM Way w")
	public Iterable<Long> findAllWayIds();
	
}
