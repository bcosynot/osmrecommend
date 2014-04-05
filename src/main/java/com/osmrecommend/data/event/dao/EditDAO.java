package com.osmrecommend.data.event.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.data.event.edit.NodeEdit;
import com.osmrecommend.data.event.edit.WayEdit;
import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.service.NodeService;
import com.osmrecommend.persistence.service.WayService;

@Component
public class EditDAO implements EventDAO {

	@Autowired
	NodeService nodeService;
	
	@Autowired
	WayService wayService;
	
	@Override
	public Cursor<Event> streamEvents() {

		List<Event> allEdits = new ArrayList<Event>();

		// Get all nodes
		for(Node node : nodeService.getAllNodes()) {
			
			allEdits.add(new NodeEdit(node));
			
		}
		
		// Get all ways
		for(Way way : wayService.getAllWays()) {
			
			allEdits.add((Event) new WayEdit(way));
			
		}
		
		return Cursors.wrap(allEdits);
	}

	@Override
	public <E extends Event> Cursor<E> streamEvents(Class<E> type) {
		return streamEvents(type, SortOrder.ANY);
	}

	@Override
	public <E extends Event> Cursor<E> streamEvents(Class<E> type,
			SortOrder order) {
		
		List<Event> allEdits = new ArrayList<Event>();

		if(type == NodeEdit.class) {
			
			// Get all nodes
			for(Node node : nodeService.getAllNodes()) {

				allEdits.add(new NodeEdit(node));

			}
			
		} else if (type == WayEdit.class) {

			// Get all ways
			for(Way way : wayService.getAllWays()) {

				allEdits.add(new WayEdit(way));

			}
		}
		
		Comparator<Event> comp = order.getEventComparator();
		Cursor<Event> cursor = Cursors.wrap(allEdits);
		
		if (comp == null) {
            return (Cursor<E>) cursor;
        } else {
            return (Cursor<E>) Cursors.sort(cursor, comp);
        }
		
	}

	/**
	 * 
	 */
	/*public EditDAO() {
		super();
		
		nodeService = new NodePersistenceServiceImpl();
		wayService = new WayPersistenceServiceImpl();
		
	}*/
	
	

}
