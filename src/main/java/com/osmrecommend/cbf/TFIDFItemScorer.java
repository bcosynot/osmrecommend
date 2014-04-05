package com.osmrecommend.cbf;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.pref.Preference;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
@Configurable
public class TFIDFItemScorer extends AbstractItemScorer {
    private final UserEventDAO dao;
    private final TFIDFModel model;

    /**
     * Construct a new item scorer.  LensKit's dependency injector will call this constructor and
     * provide the appropriate parameters.
     *
     * @param dao The user-event DAO, so we can fetch a user's ratings when scoring items for them.
     * @param m   The precomputed model containing the item tag vectors.
     */
    @Inject
    public TFIDFItemScorer(UserEventDAO dao, TFIDFModel m) {
        this.dao = dao;
        model = m;
    }

    /**
     * Generate item scores personalized for a particular user.  For the TFIDF scorer, this will
     * prepare a user profile and compare it to item tag vectors to produce the score.
     *
     * @param user   The user to score for.
     * @param output The output vector.  The contract of this method is that the caller creates a
     *               vector whose possible keys are all items that should be scored; this method
     *               fills in the scores.
     */
    @Override
    public void score(long user, @Nonnull MutableSparseVector output) {
        // Get the user's profile, which is a vector with their 'like' for each tag
        SparseVector userVector = makeUserVector(user);

        // Loop over each item requested and score it.
        // The *domain* of the output vector is the items that we are to score.
        for (VectorEntry e: output.fast(VectorEntry.State.EITHER)) {
            // Score the item represented by 'e'.
            // Get the item vector for this item
            SparseVector iv = model.getItemVector(e.getKey());
            // TODO Compute the cosine of this item and the user's profile, store it in the output vector
            double cos=0d;
            /*double num=0d;
            double denom1=0d;
            double denom2=0d;
            for(long t:iv.keySet()) {
            	double numt = userVector.get(t)*iv.get(t);
				num=num+numt;
            	double denom1t = Math.pow(userVector.get(t), 2);
				denom1=denom1+denom1t;
            	double denom2t = Math.pow(iv.get(t), 2);
				denom2=denom2+denom2t;
            }
            double denom = Math.sqrt(denom1)*Math.sqrt(denom2);
			cos=num/denom;*/
            //cos=(userVector.dot(iv))/(Math.pow(iv.norm(), 2)*Math.pow(userVector.norm(), 2));
            cos=(userVector.dot(iv))/(iv.norm()*userVector.norm());
        	output.set(e, cos);
            // TODO And remove this exception to say you've implemented it
            //throw new UnsupportedOperationException("stub implementation");
        }
    }

    private SparseVector makeUserVector(long user) {
        // Get the user's ratings
        List<Rating> userRatings = dao.getEventsForUser(user, Rating.class);
        if (userRatings == null) {
            // the user doesn't exist
            return SparseVector.empty();
        }

        // Create a new vector over tags to accumulate the user profile
        MutableSparseVector profile = model.newTagVector();
        // Fill it with 0's initially - they don't like anything
        profile.fill(0);
        
        // For 2nd part, calculate the mean of the user's rating
        double mean=0d;
        for(Rating r: userRatings) {
        	mean=mean+r.getPreference().getValue();
        }
        mean=mean/userRatings.size();
        // Iterate over the user's ratings to build their profile
        for (Rating r: userRatings) {
            // In LensKit, ratings are expressions of preference
            Preference p = r.getPreference();
            // We'll never have a null preference. But in LensKit, ratings can have null
            // preferences to express the user unrating an item
            if (p != null && p.getValue() >= 3.5) {
                // The user likes this item!
                // TODO Get the item's vector and add it to the user's profile
            	SparseVector iv = model.getItemVector(p.getItemId());
				profile.add(iv);
            }
            
            /*// For 1nd part:
            MutableSparseVector iv = model.getItemVector(p.getItemId()).mutableCopy();
        	// User vector calculation for 2nd part is different
        	for(VectorEntry v: iv.fast()) {
        		double weight = p.getValue()-mean;
				double newValue=v.getValue()*weight;
        		iv.set(v.getKey(), newValue);
        	}
			profile.add(iv.freeze());*/
            
        }

        // The profile is accumulated, return it.
        // It is good practice to return a frozen vector.
        return profile.freeze();
    }
}
