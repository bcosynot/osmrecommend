package com.osmrecommend.app;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.basic.TopNItemRecommender;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.eval.EvalConfig;
import org.grouplens.lenskit.eval.EvalProject;
import org.grouplens.lenskit.eval.TaskExecutionException;
import org.grouplens.lenskit.eval.algorithm.AlgorithmInstanceBuilder;
import org.grouplens.lenskit.eval.data.GenericDataSource;
import org.grouplens.lenskit.eval.data.crossfold.CrossfoldMethod;
import org.grouplens.lenskit.eval.data.crossfold.CrossfoldTask;
import org.grouplens.lenskit.eval.data.traintest.TTDataSet;
import org.grouplens.lenskit.eval.metrics.topn.IndependentRecallTopNMetric;
import org.grouplens.lenskit.eval.metrics.topn.ItemSelectors;
import org.grouplens.lenskit.eval.metrics.topn.PrecisionRecallTopNMetric;
import org.grouplens.lenskit.eval.traintest.LenskitTestUser;
import org.grouplens.lenskit.eval.traintest.SimpleEvaluator;
import org.grouplens.lenskit.eval.traintest.TestUser;
import org.grouplens.lenskit.util.table.Row;
import org.grouplens.lenskit.util.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.osmrecommend.cbf.TFIDFDistanceItemScorer;
import com.osmrecommend.cbf.TFIDFItemScorer;
import com.osmrecommend.cbf.TFIDFModel;
import com.osmrecommend.cbf.TFIDFModelBuilder;
import com.osmrecommend.config.JPAConfiguration;
import com.osmrecommend.dao.AreaDAO;
import com.osmrecommend.dao.CustomCrossFoldTask;
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

public class OSMRecommendEval {

	public static final Logger logger = LoggerFactory
			.getLogger(OSMRecommendEval.class);

	private static AreaDAO areaDAO;
	@Autowired
	private static NodeService nodeService;
	@Autowired
	private static WayService wayService;
	@Autowired
	private static UserService userService;

	static Long2ObjectMap<Area> allAreas = new Long2ObjectOpenHashMap<Area>();
	static Long2ObjectMap<ObjectList<String>> allNodeTagsByArea;
	static Long2ObjectMap<ObjectList<String>> allWayTagsByArea;
	static Long2LongMap nodesByArea;
	static Long2LongMap waysByArea;
	static ObjectList<User> allUsers;

	public OSMRecommendEval() {
		super();
		logger.info("inside OSMRecommendEval constructor.");
	}

	public static void main(String args[]) {

		logger.info("initialising app context..");
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		logger.info("registering jpaconfiguration");
		appContext.register(JPAConfiguration.class);
		logger.info("refreshing context");
		appContext.refresh();
		logger.info("context refreshed");

		if (null == nodeService)
			nodeService = appContext.getBean(NodeService.class);
		if (null == wayService)
			wayService = appContext.getBean(WayService.class);

		EditDAO editDAO = new EditDAO(nodeService, wayService);
		editDAO.nodeInit();
		editDAO.wayInit();

		if (null == userService)
			userService = appContext.getBean(UserService.class);

		areaDAO = appContext.getBean(AreaDAO.class);

		/* Lenkskit */

		logger.info("configuring lenskit.");
		CompositeConfiguration config = new CompositeConfiguration();
		try {
			config.addConfiguration(new PropertiesConfiguration(
					"app.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

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
				logger.info(allUsers.size() + " users fetched in " + endTime
						/ 1000 + "s");
			}
		};
		usersThread.start();

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

		try {
			logger.info("waiting for allareas");
			allAreasThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			logger.info("waiting for usersThread");
			usersThread.join();
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

		editDAO.setNodesByArea(nodesByArea);
		editDAO.setWaysByArea(waysByArea);
		
		Properties properties = new Properties();
		Integer threadCount = null;
		if (null != (threadCount = config.getInteger("threadCount", 20))) {
			properties.setProperty(EvalConfig.THREAD_COUNT_PROPERTY,
					threadCount.toString());
		}

		SimpleEvaluator simpleEval = new SimpleEvaluator(properties);
		logger.info("Building algorithm");
		AlgorithmInstanceBuilder algo = new AlgorithmInstanceBuilder("tfdidf");
		AlgorithmInstanceBuilder algo2 = new AlgorithmInstanceBuilder("tfdidfCentroidDistInverse");
		AlgorithmInstanceBuilder algo3 = new AlgorithmInstanceBuilder("tfdidfMinDistInverse");
		LenskitConfiguration lenskitConfig = algo.getConfig();
		LenskitConfiguration lenskitConfig2 = algo2.getConfig();
		LenskitConfiguration lenskitConfig3 = algo3.getConfig();

		lenskitConfig.bind(UserService.class).to(userService);
		lenskitConfig2.bind(UserService.class).to(userService);
		lenskitConfig3.bind(UserService.class).to(userService);
		lenskitConfig.bind(NodeService.class).to(nodeService);
		lenskitConfig2.bind(NodeService.class).to(nodeService);
		lenskitConfig3.bind(NodeService.class).to(nodeService);
		lenskitConfig.bind(WayService.class).to(wayService);
		lenskitConfig2.bind(WayService.class).to(wayService);
		lenskitConfig3.bind(WayService.class).to(wayService);

		lenskitConfig.bind(Event.class).to(Edit.class);
		lenskitConfig2.bind(Event.class).to(Edit.class);
		lenskitConfig3.bind(Event.class).to(Edit.class);

		lenskitConfig.bind(ItemDAO.class).to(areaDAO);
		lenskitConfig2.bind(ItemDAO.class).to(areaDAO);
		lenskitConfig3.bind(ItemDAO.class).to(areaDAO);

		lenskitConfig.bind(UserHistory.class).to(CustomUserHistory.class);
		lenskitConfig2.bind(UserHistory.class).to(CustomUserHistory.class);
		lenskitConfig3.bind(UserHistory.class).to(CustomUserHistory.class);
		lenskitConfig.bind(EventDAO.class).to(editDAO);
		lenskitConfig2.bind(EventDAO.class).to(editDAO);
		lenskitConfig3.bind(EventDAO.class).to(editDAO);

		CustomUserDAO customUserDAO = new CustomUserDAO(userService, allUsers);
		lenskitConfig.bind(UserDAO.class).to(customUserDAO);
		lenskitConfig2.bind(UserDAO.class).to(customUserDAO);
		lenskitConfig3.bind(UserDAO.class).to(customUserDAO);

		CustomUserEventDAO customUserEventDAO = new CustomUserEventDAO(
				nodeService, wayService, userService, nodesByArea, waysByArea,
				customUserDAO, editDAO);
		lenskitConfig.bind(UserEventDAO.class).to(customUserEventDAO);
		lenskitConfig2.bind(UserEventDAO.class).to(customUserEventDAO);
		lenskitConfig3.bind(UserEventDAO.class).to(customUserEventDAO);

		logger.info("Create model builder");
		Long startTime = System.currentTimeMillis();
		TFIDFModelBuilder tfidfModelBuilder = new TFIDFModelBuilder(areaDAO,
				allAreas, allNodeTagsByArea, allWayTagsByArea, nodesByArea,
				waysByArea);
		Long endTime = System.currentTimeMillis() - startTime;
		logger.info("TFIDFModelBuilder created in " + endTime / 1000 + "s");

		logger.info("Create TFIDFItemScorer");
		startTime = System.currentTimeMillis();
		TFIDFModel model = tfidfModelBuilder.get();
		TFIDFItemScorer itemScorerInstance = new TFIDFItemScorer(
				model, customUserEventDAO);
		endTime = System.currentTimeMillis() - startTime;
		logger.info("TFIDFItemScorer created in " + endTime / 1000 + "s");

		lenskitConfig.bind(ItemScorer.class).to(itemScorerInstance);
		
		TFIDFDistanceItemScorer distanceItemScorer = new TFIDFDistanceItemScorer(model, customUserEventDAO, allAreas, 1);
		lenskitConfig2.bind(ItemScorer.class).to(distanceItemScorer);
		
		TFIDFDistanceItemScorer distanceItemScorer2 = new TFIDFDistanceItemScorer(model, customUserEventDAO, allAreas, 0);
		lenskitConfig3.bind(ItemScorer.class).to(distanceItemScorer2);
		//TopNItemRecommender itemRecommender = new TopNItemRecommender(customUserEventDAO, areaDAO, itemScorerInstance);
		lenskitConfig.bind(ItemRecommender.class).to(TopNItemRecommender.class);
		lenskitConfig2.bind(ItemRecommender.class).to(TopNItemRecommender.class);
		lenskitConfig3.bind(ItemRecommender.class).to(TopNItemRecommender.class);
		lenskitConfig.bind(TestUser.class).to(LenskitTestUser.class);
		lenskitConfig2.bind(TestUser.class).to(LenskitTestUser.class);
		lenskitConfig3.bind(TestUser.class).to(LenskitTestUser.class);
		lenskitConfig.bind(CrossfoldTask.class).to(CustomCrossFoldTask.class);
		lenskitConfig2.bind(CrossfoldTask.class).to(CustomCrossFoldTask.class);
		lenskitConfig3.bind(CrossfoldTask.class).to(CustomCrossFoldTask.class);
		simpleEval.addAlgorithm(algo.build());
		simpleEval.addAlgorithm(algo2.build());
		simpleEval.addAlgorithm(algo3.build());

		GenericDataSource gds = new GenericDataSource("customgds6", editDAO);
		CustomCrossFoldTask crossFoldTask = new CustomCrossFoldTask(
				"crossfold6", customUserDAO, customUserEventDAO);
		crossFoldTask.setHoldoutFraction(0.2f);
		crossFoldTask.setMethod(CrossfoldMethod.PARTITION_USERS);
		crossFoldTask.setSource(gds);
		crossFoldTask.setPartitions(2);
		if (null != simpleEval.getRawCommand()) {
			EvalProject project = simpleEval.getRawCommand().getProject();
			if (null != project) {
				logger.info("setting project");
				crossFoldTask.setProject(project);
			}
		}
		try {
			for (TTDataSet dataSet : crossFoldTask.perform()) {
				simpleEval.addDataset(dataSet);
			}
		} catch (TaskExecutionException e1) {
			e1.printStackTrace();
		}
		PrecisionRecallTopNMetric precisionRecallTopNMetric = new PrecisionRecallTopNMetric(
				null, null, 15, ItemSelectors.allItems(), ItemSelectors.nRandom(0),
				ItemSelectors.trainingItems());
		simpleEval.addMetric(precisionRecallTopNMetric);

		simpleEval.setOutput(new File("eval-results6.csv"));

		try {
			logger.info("Starting evaluations");
			Table table = simpleEval.call();
			logger.info("evaluation over");
			Iterator<Row> rowIterator = table.iterator();
			logger.info("printing details");
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				Iterator<Object> rowObjectIterator = row.iterator();

				while (rowObjectIterator.hasNext()) {

					Object o = rowObjectIterator.next();

					logger.info("" + o);

				}

			}
		} catch (TaskExecutionException e) {
			e.printStackTrace();
		}
		appContext.close();

	}
}
