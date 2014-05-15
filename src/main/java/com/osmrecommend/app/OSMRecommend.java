package com.osmrecommend.app;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.List;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.basic.TopNItemRecommender;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.scored.ScoredId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.osmrecommend.cbf.TFIDFItemScorer;
import com.osmrecommend.cbf.TFIDFModelBuilder;
import com.osmrecommend.config.JPAConfiguration;
import com.osmrecommend.dao.AreaDAO;
import com.osmrecommend.dao.CustomUserDAO;
import com.osmrecommend.data.event.CustomUserHistory;
import com.osmrecommend.data.event.Edit;
import com.osmrecommend.data.event.dao.CustomUserEventDAO;
import com.osmrecommend.data.event.dao.EditDAO;
import com.osmrecommend.persistence.domain.Area;
import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.service.NodeService;
import com.osmrecommend.persistence.service.UserService;
import com.osmrecommend.persistence.service.WayService;

//@ContextConfiguration(classes = {JPAConfiguration.class})
@Component
public class OSMRecommend {

	private static Logger logger = LoggerFactory.getLogger(OSMRecommend.class);

	private static AreaDAO areaDAO;
	@Autowired
	private static NodeService nodeService;
	@Autowired
	private static WayService wayService;
	@Autowired
	private static UserService userService;

	static Long2ObjectMap<Area> allAreas = new Long2ObjectOpenHashMap<Area>();
	static Long2ObjectMap<ObjectList<String>> allNodeTagsByArea = null;
	static Long2ObjectMap<ObjectList<String>> allWayTagsByArea = null;
	static Long2LongMap nodesByArea = null;
	static Long2LongMap waysByArea = null;
	static ObjectList<User> allUsers = null;

	public static void main(String args[]) {

		logger.info("initialising app context..");
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		logger.info("registering jpaconfiguration");
		appContext.register(JPAConfiguration.class);
		logger.info("refreshing context");
		appContext.refresh();
		logger.info("context refreshed");
		areaDAO = appContext.getBean(AreaDAO.class);
		if (null == nodeService)
			nodeService = appContext.getBean(NodeService.class);
		if (null == wayService)
			wayService = appContext.getBean(WayService.class);
		if (null == userService)
			userService = appContext.getBean(UserService.class);

		/* Lenkskit */

		logger.info("configuring lenskit.");
		final LenskitConfiguration lenskitConfig = new LenskitConfiguration();

		lenskitConfig.bind(UserService.class).to(userService);
		lenskitConfig.bind(NodeService.class).to(nodeService);
		lenskitConfig.bind(WayService.class).to(wayService);

		lenskitConfig.bind(Event.class).to(Edit.class);

		lenskitConfig.bind(UserDAO.class).to(CustomUserDAO.class);

		lenskitConfig.bind(ItemDAO.class).to(areaDAO);

		lenskitConfig.bind(UserHistory.class).to(CustomUserHistory.class);


		// Use item-item CF to score items
		Thread allAreasThread = new Thread() {
			@Override
			public void run() {
				super.run();
				logger.info("fetch all areas");
				Long allAreasStartTime = System.currentTimeMillis();
				allAreas = areaDAO.getAllAreasMap();
				Long endTime = System.currentTimeMillis() - allAreasStartTime;
				logger.info(allAreas.size() + " areas fetched in " + endTime
						/ 1000 + "s");
			}
		};
		allAreasThread.start();

		Thread allNodeTags = new Thread() {
			@Override
			public void run() {
				super.run();
				logger.info("fetch all node tags");
				Long allNodeTagsStartTime = System.currentTimeMillis();
				allNodeTagsByArea = areaDAO.getAllNodeTagsByArea();
				Long endTime = System.currentTimeMillis()
						- allNodeTagsStartTime;
				logger.info("node tags fetched in " + endTime / 1000 + "s");
			}
		};
		allNodeTags.start();

		Thread allWayTagsThread = new Thread() {
			@Override
			public void run() {
				super.run();
				logger.info("fetch all way tags");
				Long allWayTagsStartTime = System.currentTimeMillis();
				allWayTagsByArea = areaDAO.getAllWayTagsByArea();
				Long endTime = System.currentTimeMillis() - allWayTagsStartTime;
				logger.info("way tags fetched in " + endTime / 1000 + "s");
			}
		};
		allWayTagsThread.start();

		Thread nodesByAreaThread = new Thread() {
			@Override
			public void run() {
				super.run();
				logger.info("fetch all nodes by areas");
				Long nodesByAreaStartTime = System.currentTimeMillis();
				nodesByArea = areaDAO.getNodesByArea();
				Long endTime = System.currentTimeMillis()
						- nodesByAreaStartTime;
				logger.info("nodes fetched in " + endTime / 1000 + "s");
			}
		};
		nodesByAreaThread.start();

		Thread waysByAreaThread = new Thread() {
			@Override
			public void run() {
				super.run();
				logger.info("fetch all ways by areas");
				Long waysByAreaStartTime = System.currentTimeMillis();
				waysByArea = areaDAO.getWaysByArea();
				Long endTime = System.currentTimeMillis() - waysByAreaStartTime;
				logger.info("ways fetched in " + endTime / 1000 + "s");
			}
		};
		waysByAreaThread.start();
		
		Thread usersThread = new Thread() {
			@Override
			public void run() {
				super.run();
				logger.info("fetch all users");
				Long usersStartTime = System.currentTimeMillis();
				allUsers = userService.getAllUsers();
				Long endTime = System.currentTimeMillis() - usersStartTime;
				logger.info("user fetched in " + endTime / 1000 + "s");
			}
		};
		usersThread.start();

		try {
			logger.info("waiting for allareas");
			allAreasThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			logger.info("waiting for allNodeTags");
			allNodeTags.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			logger.info("waiting for allWayTagsThread");
			allWayTagsThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			logger.info("waiting for nodesByAreaThread");
			nodesByAreaThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			logger.info("waiting for waysByAreaThread");
			waysByAreaThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			logger.info("waiting for usersThread");
			usersThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		EditDAO editDAO = new EditDAO(nodeService, wayService, nodesByArea, waysByArea);
		lenskitConfig.bind(EventDAO.class).to(editDAO);
		
		CustomUserDAO customUserDAO = new CustomUserDAO(userService, allUsers);
		lenskitConfig.bind(UserDAO.class).to(customUserDAO);
		
		CustomUserEventDAO customUserEventDAO = new CustomUserEventDAO(nodeService, wayService, userService, nodesByArea, waysByArea, customUserDAO, editDAO);
		lenskitConfig.bind(UserEventDAO.class).to(customUserEventDAO);

		logger.info("Create model builder");
		Long startTime = System.currentTimeMillis();
		TFIDFModelBuilder tfidfModelBuilder = new TFIDFModelBuilder(areaDAO,
				allAreas, allNodeTagsByArea, allWayTagsByArea, nodesByArea,
				waysByArea);
		Long endTime = System.currentTimeMillis() - startTime;
		logger.info("TFIDFModelBuilder created in " + endTime / 1000 + "s");

		logger.info("Create TFIDFItemScorer");
		startTime = System.currentTimeMillis();
		TFIDFItemScorer itemScorerInstance = new TFIDFItemScorer(
				tfidfModelBuilder.get(), customUserEventDAO);
		endTime = System.currentTimeMillis() - startTime;
		logger.info("TFIDFItemScorer created in " + endTime / 1000 + "s");

		lenskitConfig.bind(ItemScorer.class).to(itemScorerInstance);

		lenskitConfig.bind(ItemRecommender.class).to(TopNItemRecommender.class);

		try {
			logger.info("creating recommender");
			LenskitRecommender rec = LenskitRecommender.build(lenskitConfig);
			TopNItemRecommender irec = (TopNItemRecommender) rec
					.getItemRecommender();
			logger.info("fetching recommendations");
			List<ScoredId> recommendations = irec.recommend(1725093, 10);
			logger.info("recos fetched");
			for (ScoredId scoredId : recommendations) {
				logger.info(scoredId.getId() + " - " + scoredId.getScore());
			}
		} catch (RecommenderBuildException e) {
			e.printStackTrace();
		}
		
		appContext.close();
	}
}
