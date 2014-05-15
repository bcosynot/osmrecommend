/**
 * 
 */
package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.osmrecommend.persistence.domain.Area;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Vivek
 *
 */
public interface AreaRepository extends PagingAndSortingRepository<Area, Long> {

	@Query("SELECT a.id FROM Area a")
	public ObjectList<Long> fetchAllIds();
	
	@Query("SELECT n.tags FROM Node n WHERE within(n.geom, :geom) = true")
	public Iterable<Object2ObjectMap<String, String>> findAllNodeTags(@Param("geom") Geometry geom);
	
	@Query("SELECT w.tags FROM Way w WHERE within(w.bbox, :geom) = true")
	public Iterable<Object2ObjectMap<String, String>> findAllWaysTags(@Param("geom") Geometry geom);
	
	@Query("SELECT n.user.id FROM Node n WHERE within(n.geom, :geom) = true")
	public Iterable<Long> findAllNodeUserIds(@Param("geom") Geometry geom);
	
	@Query("SELECT w.user.id FROM Way w WHERE within(w.bbox, :geom) = true")
	public Iterable<Long> findAllWaysUserIds(@Param("geom") Geometry geom);
	
	
}
