/**
 * 
 */
package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Area;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Vivek
 *
 */
@Repository
public interface AreaRepository extends PagingAndSortingRepository<Area, Long> {

	@Query("SELECT a.id FROM Area a")
	public List<Long> fetchAllIds();
	
	@Query("SELECT n.tags FROM Node n WHERE intersects(n.geom, :geom) = true")
	public Iterable<Object2ObjectMap<String, String>> findAllNodeTags(@Param("geom") Geometry geom);
	
	@Query("SELECT w.tags FROM Way w WHERE intersects(w.bbox, :geom) = true")
	public Iterable<Object2ObjectMap<String, String>> findAllWaysTags(@Param("geom") Geometry geom);
	
	@Query("SELECT n.user.id FROM Node n WHERE intersects(n.geom, :geom) = true")
	public Iterable<Long> findAllNodeUserIds(@Param("geom") Geometry geom);
	
	@Query("SELECT w.user.id FROM Way w WHERE intersects(w.bbox, :geom) = true")
	public Iterable<Long> findAllWaysUserIds(@Param("geom") Geometry geom);
	
	
}
