package com.osmrecommend.cbf;

import it.unimi.dsi.fastutil.objects.ObjectList;

import javax.annotation.Nonnull;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import com.osmrecommend.data.event.CustomUserHistory;
import com.osmrecommend.data.event.Edit;
import com.osmrecommend.data.event.dao.CustomUserEventDAO;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
@Configurable
public class TFIDFItemScorer extends AbstractItemScorer {

	private static final Logger logger = LoggerFactory
			.getLogger(TFIDFItemScorer.class);

	protected CustomUserEventDAO dao;

	protected TFIDFModel model;

	/**
	 * Construct a new item scorer. LensKit's dependency injector will call this
	 * constructor and provide the appropriate parameters.
	 * 
	 * @param customUserEventDAO
	 * 
	 * @param dao
	 *            The user-event DAO, so we can fetch a user's ratings when
	 *            scoring items for them.
	 * @param m
	 *            The precomputed model containing the item tag vectors.
	 */
	public TFIDFItemScorer(TFIDFModel model,
			CustomUserEventDAO customUserEventDAO) {

		logger.info("Creating instance of TFIDFItemscorer");
		this.model = model;
		if (null == this.dao)
			this.dao = customUserEventDAO;
	}

	/**
	 * Generate item scores personalized for a particular user. For the TFIDF
	 * scorer, this will prepare a user profile and compare it to item tag
	 * vectors to produce the score.
	 * 
	 * @param user
	 *            The user to score for.
	 * @param output
	 *            The output vector. The contract of this method is that the
	 *            caller creates a vector whose possible keys are all items that
	 *            should be scored; this method fills in the scores.
	 */
	@Override
	public void score(long user, @Nonnull MutableSparseVector output) {

		// Get the user's profile, which is a vector with their 'like' for each
		// tag
		SparseVector userVector = makeUserVector(user);

		// Loop over each item requested and score it.
		// The *domain* of the output vector is the items that we are to score.
		for (VectorEntry e : output.fast(VectorEntry.State.EITHER)) {
			// Score the item represented by 'e'.
			// Get the item vector for this item
			SparseVector iv = model.getItemVector(e.getKey());
			double cos = (userVector.dot(iv)) / (iv.norm() * userVector.norm());
			output.set(e, cos);
		}
	}

	private SparseVector makeUserVector(long user) {

		// Get the user's ratings
		ObjectList<Edit> userEvent = null;
		CustomUserHistory<Edit> customUserHistory = null;
		if (null == dao) {
			logger.info("dao is null");
		} 
		if (null == (customUserHistory = dao.getCustomEventsForUser(user))) {
			//logger.info("the user doesn't exist");
			return SparseVector.empty();
		} else {
			userEvent = customUserHistory.getEventHistory();
		}
		if (null == model) {
			logger.info("model is null");
		} else {
			if (null == model.getNodesByArea()) {
				logger.info("nodesbyarea in model is null");
			} 
			if (null == model.getWaysByArea()) {
				logger.info("getWaysByArea in model is null");
			} 
		}

		// Create a new vector over tags to accumulate the user profile
		MutableSparseVector profile = model.newTagVector();
		// Fill it with 0's initially - they don't like anything
		profile.fill(0);

		// Iterate over the user's ratings to build their profile
		for (Edit e : userEvent) {

			if (e != null) {
				// The user likes this item!
				SparseVector iv = null;
				Long itemId = null;
				if (null != (itemId = e.getItemId())) {
					iv = model.getItemVector(itemId);
					if (null != iv) {
						profile.add(iv);
					}
				} else {
					logger.info("Item id is null");
				}
			}

			// For 1nd part:
			/*
			 * MutableSparseVector iv = model.getItemVector(e.getItemId())
			 * .mutableCopy(); // User vector calculation for 2nd part is
			 * different double mean = iv.mean(); for (VectorEntry v :
			 * iv.fast()) {
			 * 
			 * double weight = v.getValue() - mean; double newValue =
			 * v.getValue() * weight; iv.set(v.getKey(), newValue);
			 * 
			 * }
			 * 
			 * profile.add(iv.freeze());
			 */

		}

		// The profile is accumulated, return it.
		// It is good practice to return a frozen vector.
		return profile.freeze();
	}
}
