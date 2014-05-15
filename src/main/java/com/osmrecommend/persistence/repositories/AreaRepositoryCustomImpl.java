/**
 * 
 */
package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.osmrecommend.util.HstoreHelper;

/**
 * @author vranjan
 * 
 */
@Component
public class AreaRepositoryCustomImpl implements AreaRepositoryCustom {
	
	public static Logger logger = LoggerFactory.getLogger(AreaRepositoryCustomImpl.class);

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("rawtypes")
	@Override
	public Long2LongMap fetchAllNodes() {
		
		Long2LongMap nodesByArea = new Long2LongOpenHashMap();

		String sqlQuery = "SELECT a.id, n.id FROM Area a, Node n WHERE intersects(a.theGeom, n.geom) = true";
		Query query = em.createQuery(sqlQuery);
		List results = query.getResultList();
		
		if (null != results) {
			logger.info("results size:"+results.size());
			Iterator resultsIterator = results.iterator();
			
			while (resultsIterator.hasNext()) {
				
				Object[] result = (Object[]) resultsIterator.next();
				Long areaId = (Long) result[0];
				Long nodeId = (Long) result[1];
				nodesByArea.put(nodeId, areaId);
			}
		} else {
			logger.info("null result");
		}
		
		return nodesByArea;

		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Long2ObjectMap<ObjectList<String>> fetchAllNodeTags() {

		Long2ObjectMap<ObjectList<String>> nodeTagsByArea = new Long2ObjectOpenHashMap<ObjectList<String>>();

		String sqlQuery = "SELECT a.id, n.tags FROM Area a, Node n WHERE intersects(a.theGeom, n.geom) = true ORDER BY a.id";
		Query query = em.createQuery(sqlQuery);
		List results = query.getResultList();
		
		if (null != results) {
			logger.info("results size:"+results.size());
			Iterator resultsIterator = results.iterator();
			Long currentId = 0l;
			Long previousId = 0l;
			ObjectList<String> allTags = new ObjectArrayList<String>();
			while (resultsIterator.hasNext()) {
				
				Object[] result = (Object[]) resultsIterator.next();
				Long areadId = (Long) result[0];
				String tags = (String) result[1];
				
				currentId = areadId;
				if (previousId != 0l && (currentId != previousId)) {
					nodeTagsByArea.put(previousId, allTags);
					allTags = new ObjectArrayList<String>();
				}
				Object2ObjectOpenHashMap<String,String> map = HstoreHelper.toMap(tags);
				allTags.addAll(map.keySet());
				allTags.addAll(map.values());
				previousId = currentId;
				
			}
		} else {
			logger.info("null result");
		}
		
		return nodeTagsByArea;

	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Long2LongMap fetchAllWays() {
		
		Long2LongMap waysByArea = new Long2LongOpenHashMap();

		String sqlQuery = "SELECT a.id, w.id FROM Area a, Way w WHERE intersects(a.theGeom, w.bbox) = true";
		Query query = em.createQuery(sqlQuery);
		List results = query.getResultList();
		
		if (null != results) {
			logger.info("results size:"+results.size());
			Iterator resultsIterator = results.iterator();
			
			while (resultsIterator.hasNext()) {
				
				Object[] result = (Object[]) resultsIterator.next();
				Long areaId = (Long) result[0];
				Long wayId = (Long) result[1];
				waysByArea.put(wayId, areaId);
			}
		} else {
			logger.info("null result");
		}
		
		return waysByArea;

		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Long2ObjectMap<ObjectList<String>> fetchAllWayTags() {

		Long2ObjectMap<ObjectList<String>> wayTagsByArea = new Long2ObjectOpenHashMap<ObjectList<String>>();

		String sqlQuery = "SELECT a.id, w.tags FROM Area a, Way w WHERE intersects(a.theGeom, w.bbox) = true ORDER BY a.id";
		Query query = em.createQuery(sqlQuery);
		List results = query.getResultList();
		
		if (null != results) {
			logger.info("results size:"+results.size());
			Iterator resultsIterator = results.iterator();
			Long currentId = 0l;
			Long previousId = 0l;
			ObjectList<String> allTags = new ObjectArrayList<String>();
			while (resultsIterator.hasNext()) {
				
				Object[] result = (Object[]) resultsIterator.next();
				Long areadId = (Long) result[0];
				String tags = (String) result[1];
				
				currentId = areadId;
				if (previousId != 0l && (currentId != previousId)) {
					wayTagsByArea.put(previousId, allTags);
					allTags = new ObjectArrayList<String>();
				}
				Object2ObjectOpenHashMap<String,String> map = HstoreHelper.toMap(tags);
				allTags.addAll(map.keySet());
				allTags.addAll(map.values());
				previousId = currentId;
				
			}
		} else {
			logger.info("null result");
		}
		
		return wayTagsByArea;

	}
	
}
