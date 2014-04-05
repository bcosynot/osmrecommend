package com.osmrecommend.app;

import java.util.List;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.scored.ScoredId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.osmrecommend.cbf.TFIDFItemScorer;
import com.osmrecommend.config.JPAConfiguration;
import com.osmrecommend.dao.NodeDAO;
import com.osmrecommend.dao.OSMRecommendUserDAO;
import com.osmrecommend.data.event.dao.EditDAO;
import com.osmrecommend.data.event.edit.NodeEdit;

//@ContextConfiguration(classes = {JPAConfiguration.class})
@Component
public class OSMRecommend {
	
	private static Logger log = LoggerFactory.getLogger(OSMRecommend.class);
		
	public static void main(String args[]) {
		
		log.debug("initialising app context..");
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.register(JPAConfiguration.class);
		appContext.refresh();
		//JPAConfiguration jpaConfig = appContext.getBean(JPAConfiguration.class);
		
		/*Lenkskit*/
		
		log.info("configuring lenskit.");
		final LenskitConfiguration lenskitConfig = new LenskitConfiguration();
		
		// Use item-item CF to score items
		lenskitConfig.bind(ItemScorer.class)
		      .to(TFIDFItemScorer.class);
		
		lenskitConfig.bind(EventDAO.class).to(appContext.getBean(EditDAO.class));
		
		lenskitConfig.bind(Event.class).to(NodeEdit.class);
		
		lenskitConfig.bind(UserDAO.class).to(OSMRecommendUserDAO.class);
		
		lenskitConfig.bind(ItemDAO.class).to(appContext.getBean(NodeDAO.class));

		try {
			log.info("creating recommender");
			LenskitRecommender rec = LenskitRecommender.build(lenskitConfig);
			ItemRecommender irec = rec.getItemRecommender();
			log.info("fetching recommendations");
			List<ScoredId> recommendations = irec.recommend(1538030, 5);
			log.info("recos fetched");
			for(ScoredId scoredId : recommendations) {
				System.out.println(scoredId.getId() +" - "+scoredId.getScore());
			}
		} catch (RecommenderBuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create the recommender
		/*log.info("emf...");
		EntityManager manager;
		try {
			manager = jpaConfig.entityManager(jpaConfig.entityManagerFactory());
			SessionImpl session = (SessionImpl) manager.unwrap(Session.class);
			session.doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {
					try {
						log.info("creating recommender");
						LenskitRecommender rec = LenskitRecommender.build(lenskitConfig);
						ItemRecommender irec = rec.getItemRecommender();
						log.info("fetching recommendations");
						List<ScoredId> recommendations = irec.recommend(1538030, 5);
						log.info("recos fetched");
						for(ScoredId scoredId : recommendations) {
							System.out.println(scoredId.getId() +" - "+scoredId.getScore());
						}
					} catch (RecommenderBuildException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
	}	
	
}
