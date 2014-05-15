package com.osmrecommend.cbf;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.List;

import org.grouplens.lenskit.basic.AbstractItemRecommender;
import org.grouplens.lenskit.scored.ScoredId;

public class ItemRecommenderImpl extends AbstractItemRecommender {

	@Override
	protected List<ScoredId> recommend(long user, int n, LongSet candidates,
			LongSet exclude) {
		// TODO Auto-generated method stub
		return null;
	}

}
