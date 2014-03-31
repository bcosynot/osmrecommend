package com.osmrecommend.data.event.edit;

import org.apache.commons.lang3.builder.Builder;

public class NodeEditBuilder implements Builder<NodeEdit> {
	
	private boolean hasUserId;
    private int userId;
    private boolean hasItemId;
    private long itemId;
    private long timestamp;

	/**
	 * @return the hasUserId
	 */
	public boolean isHasUserId() {
		return hasUserId;
	}

	/**
	 * @param hasUserId the hasUserId to set
	 */
	public void setHasUserId(boolean hasUserId) {
		this.hasUserId = hasUserId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the hasItemId
	 */
	public boolean isHasItemId() {
		return hasItemId;
	}

	/**
	 * @param hasItemId the hasItemId to set
	 */
	public void setHasItemId(boolean hasItemId) {
		this.hasItemId = hasItemId;
	}

	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public NodeEdit build() {
		// TODO Auto-generated method stub
		return null;
	}

}
