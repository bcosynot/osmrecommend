package com.osmrecommend.data.dao;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.List;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.ItemEventCollection;
import org.grouplens.lenskit.data.history.UserHistory;

import com.osmrecommend.data.event.dao.NodeEditDAO;
import com.osmrecommend.data.event.dao.WayEditDAO;

public class OSMRecommendCustomDAO implements EventDAO, UserEventDAO, NodeEditDAO, WayEditDAO, UserDAO, NodeDAO, WayDAO {

	
}
