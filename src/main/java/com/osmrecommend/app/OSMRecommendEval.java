package com.osmrecommend.app;

import java.util.Iterator;

import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.eval.TaskExecutionException;
import org.grouplens.lenskit.eval.algorithm.AlgorithmInstanceBuilder;
import org.grouplens.lenskit.eval.data.GenericDataSource;
import org.grouplens.lenskit.eval.traintest.SimpleEvaluator;
import org.grouplens.lenskit.util.table.Row;
import org.grouplens.lenskit.util.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.osmrecommend.cbf.TFIDFItemScorer;
import com.osmrecommend.config.JPAConfiguration;
import com.osmrecommend.dao.CustomUserDAO;
import com.osmrecommend.dao.WayDAO;
import com.osmrecommend.data.event.dao.EditDAO;
import com.osmrecommend.data.event.edit.WayEdit;

@Component
public class OSMRecommendEval {

	public static final Logger logger = LoggerFactory.getLogger(OSMRecommendEval.class);
	
	public static void main(String args[]) { 

		logger.debug("initialising app context..");
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.register(JPAConfiguration.class);
		appContext.refresh();
		
		/*Lenkskit*/
		
		logger.info("configuring lenskit.");
		
		SimpleEvaluator simpleEval = new SimpleEvaluator();
		AlgorithmInstanceBuilder algo = new AlgorithmInstanceBuilder("tfdidf");
		LenskitConfiguration lenskitConfig = algo.getConfig();
		
		// Use item-item CF to score items
		lenskitConfig.bind(ItemScorer.class).to(TFIDFItemScorer.class);

		lenskitConfig.bind(EventDAO.class)
				.to(appContext.getBean(EditDAO.class));

		lenskitConfig.bind(Event.class).to(WayEdit.class);

		lenskitConfig.bind(UserDAO.class).to(CustomUserDAO.class);

		lenskitConfig.bind(ItemDAO.class).to(appContext.getBean(WayDAO.class));
		
		simpleEval.addAlgorithm(algo);
		GenericDataSource gds = new GenericDataSource("customgds", appContext.getBean(EditDAO.class));
		simpleEval.addDataset(gds, 10);
		try {
			logger.info("Starting evaluations");
			Table table = simpleEval.call();
			logger.info("evaluation over");
			Iterator<Row> rowIterator = table.iterator();
			logger.info("printing details");
			while(rowIterator.hasNext()) {
				
				Row row = rowIterator.next();
				
				Iterator<Object> rowObjectIterator = row.iterator();
				
				while(rowObjectIterator.hasNext()) {
					
					Object o = rowObjectIterator.next();
					
					logger.info(""+o);
					
				}
				
			}
		} catch (TaskExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
