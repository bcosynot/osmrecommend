package com.osmrecommend.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.osmrecommend.config.JPAConfiguration;
import com.osmrecommend.data.event.dao.EditDAO;

//@ContextConfiguration(classes = {JPAConfiguration.class})
@Configurable
public class OSMRecommend {
	
	private static Logger log = LoggerFactory.getLogger(OSMRecommend.class);
	
	@Autowired
	static EditDAO editDAO;
	
	/*@Autowired
	static JPAConfiguration jpaConfig;*/
	
	public static void main(String args[]) {
		
		log.debug("initialising app..");
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.register(JPAConfiguration.class);
		appContext.refresh();
		JPAConfiguration jpaConfig = appContext.getBean(JPAConfiguration.class);
		
		/*Lenkskit*/
		
		log.info("configuring lenskit.");
		final LenskitConfiguration lenskitConfig = new LenskitConfiguration();
		
		// Use item-item CF to score items
		lenskitConfig.bind(ItemScorer.class)
		      .to(ItemItemScorer.class);
		
		lenskitConfig.bind(EventDAO.class).to(appContext.getBean(EditDAO.class));
		
		//lenskitConfig.bind(Event.class).to(NodeEdit.class);
		
		// Create the recommender
		log.info("emf...");
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
		}
		
	}	
	
}
