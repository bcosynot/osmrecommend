package com.osmrecommend.cbf;

import org.grouplens.grapht.annotation.DefaultProvider;
import org.grouplens.lenskit.core.Shareable;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

import java.io.Serializable;
import java.util.Map;

/**
 * The model for a TF-IDF recommender.  The model just remembers the normalized tag vector for each
 * item.
 *
 * @see TFIDFModelBuilder
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
// LensKit models are annotated with @Shareable so they can be serialized and reused
@Shareable
// This model class will be built by the model builder
@DefaultProvider(TFIDFModelBuilder.class)
public class TFIDFModel implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(TFIDFModel.class);

    private final Map<String, Long> tagIds;
    private final Map<Long, SparseVector> itemVectors;
    private Long2LongMap nodesByArea;
	private Long2LongMap waysByArea;

    /**
     * Constructor for the model.  This is package-private; the only way to build a model is with
     * the {@linkplain TFIDFModelBuilder model builder}.
     * <p>
     * In a LensKit model designed for a large data set, these would be optimized fastutil maps for
     * efficiency.
     *
     * @param tagIds A map of tags to their IDs.
     * @param itemVectors A map of item IDs to tag vectors.
     */
    TFIDFModel(Object2LongMap<String> tagIds, Long2ObjectMap<SparseVector> itemVectors) {
    	
    	logger.info("Creating instance of TFIDFModel.");
    	logger.info("Total tag ids: "+tagIds.size());
    	logger.info("total item vectors: "+itemVectors.size());
        this.tagIds = tagIds;
        this.itemVectors = itemVectors;
    }

    public TFIDFModel(Object2LongMap<String> tagIds,
			Long2ObjectMap<SparseVector> itemVectors, Long2LongMap nodesByArea,
			Long2LongMap waysByArea) {
    	logger.info("Creating instance of TFIDFModel.");
    	logger.info("Total tag ids: "+tagIds.size());
    	logger.info("total item vectors: "+itemVectors.size());
        this.tagIds = tagIds;
        this.itemVectors = itemVectors;
        this.nodesByArea = nodesByArea;
        this.waysByArea = waysByArea;
	}

	/**
     * Create a new mutable vector over all tag IDs.  The vector is initially empty, and its key
     * domain is the set of all tag IDs.
     *
     * @return A fresh vector over tag IDs.
     */
    public MutableSparseVector newTagVector() {
        return MutableSparseVector.create(tagIds.values());
    }

    /**
     * Get the tag vector for a particular item.
     *
     * @param item The item.
     * @return The item's tag vector.  If the item is not known to the model, then this vector is
     *         empty.
     */
    public SparseVector getItemVector(long item) {
        // Look up the item
        SparseVector vec = itemVectors.get(item);
        if (vec == null) {
            // We don't know the item! Return an empty vector
            return SparseVector.empty();
        } else {
            return vec;
        }
    }

	/**
	 * @return the nodesByArea
	 */
	public Long2LongMap getNodesByArea() {
		return nodesByArea;
	}

	/**
	 * @param nodesByArea the nodesByArea to set
	 */
	public void setNodesByArea(Long2LongMap nodesByArea) {
		this.nodesByArea = nodesByArea;
	}

	/**
	 * @return the waysByArea
	 */
	public Long2LongMap getWaysByArea() {
		return waysByArea;
	}

	/**
	 * @param waysByArea the waysByArea to set
	 */
	public void setWaysByArea(Long2LongMap waysByArea) {
		this.waysByArea = waysByArea;
	}
}
