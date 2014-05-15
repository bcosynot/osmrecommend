/**
 * 
 */
package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.Long2IntMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.grouplens.lenskit.collections.CollectionUtils;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.eval.TaskExecutionException;
import org.grouplens.lenskit.eval.data.crossfold.CrossfoldTask;
import org.grouplens.lenskit.eval.data.crossfold.HoldoutNPartition;
import org.grouplens.lenskit.eval.data.crossfold.Order;
import org.grouplens.lenskit.eval.data.crossfold.PartitionAlgorithm;
import org.grouplens.lenskit.eval.data.crossfold.RandomOrder;
import org.grouplens.lenskit.eval.data.traintest.TTDataSet;
import org.grouplens.lenskit.util.table.writer.TableWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.osmrecommend.data.event.CustomUserHistory;
import com.osmrecommend.data.event.Edit;
import com.osmrecommend.data.event.dao.CustomUserEventDAO;

/**
 * @author vranjan
 * 
 */
public class CustomCrossFoldTask extends CrossfoldTask {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomCrossFoldTask.class);

	private final PartitionAlgorithm<Edit> partitionMethod = new HoldoutNPartition<Edit>(
			10);
	private Order<Edit> order = new RandomOrder<Edit>();
	CustomUserDAO customUserDAO;
	CustomUserEventDAO customUserEventDAO;

	public CustomCrossFoldTask(String n, CustomUserDAO customUserDAO,
			CustomUserEventDAO customUserEventDAO) {
		super(n);
		this.customUserDAO = customUserDAO;
		this.customUserEventDAO = customUserEventDAO;
	}

	@Override
	public List<TTDataSet> perform() throws TaskExecutionException {
		logger.info("inside perform");
		try {
			createTTFiles();
		} catch (IOException ex) {
			throw new TaskExecutionException("Error writing data sets", ex);
		}
		return getTTFiles();
	}

	@Override
	protected void writeTTFilesByUsers(TableWriter[] trainWriters,
			TableWriter[] testWriters) throws TaskExecutionException {
		logger.info("writing writeTTFilesByUsers");
		Long2IntMap splits = splitUsers(customUserDAO);
		Cursor<CustomUserHistory<Edit>> historyCursor = customUserEventDAO
				.streamCustomEventsByUser();
		// Holdout mode = this.getHoldout();
		int nulls = 0;
		try {
			for (CustomUserHistory<Edit> history : historyCursor) {
				if (null == history) {
					// logger.info("history null");
					nulls++;
					continue;
				}
				Long userId = null;
				int foldNum = 0;
				if (null != (userId = history.getUserId())) {
					foldNum = splits.get(userId);
				}
				List<Edit> ratings = new ArrayList<Edit>();
				ratings.addAll(history.getEventHistory());
				final int n = ratings.size();
				for (int f = 0; f < getPartitionCount(); f++) {
					if (f == foldNum) {
						order.apply(ratings, getProject().getRandom());
						final int p = partitionMethod.partition(ratings);
						// final int p = mode.partition(ratings,
						// getProject().getRandom());
						for (int j = 0; j < p; j++) {
							writeRating(trainWriters[f], ratings.get(j));
						}
						for (int j = p; j < n; j++) {
							writeRating(testWriters[f], ratings.get(j));
						}
					} else {
						for (Edit rating : CollectionUtils.fast(ratings)) {
							writeRating(trainWriters[f], rating);
						}
					}
				}

			}
		} catch (IOException e) {
			throw new TaskExecutionException(
					"Error writing to the train test files", e);
		} finally {
			historyCursor.close();
		}
		logger.info("nulls : " + nulls);

	}

	protected void writeRating(TableWriter writer, Edit rating)
			throws IOException {
		writer.writeRow(Lists.newArrayList(Long.toString(rating.getUserId()),
				Long.toString(rating.getItemId()), "NaN",
				Long.toString(rating.getTimestamp())));
	}

}
