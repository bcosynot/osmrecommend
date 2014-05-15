package com.osmrecommend.data.event.edit;

import it.unimi.dsi.fastutil.longs.Long2LongMap;

import com.osmrecommend.data.event.Edit;
import com.osmrecommend.persistence.domain.Node;

public class NodeEdit extends Edit {

	Node node;

	Long itemId;

	Long userId;

	Long tstamp;

	Long2LongMap nodesByArea;

	@Override
	public long getUserId() {
		return node.getUser().getId();
	}

	@Override
	public long getItemId() {
		return node.getId();
	}

	@Override
	public long getTimestamp() {
		return tstamp;
	}

	public NodeEdit(Long2LongMap nodesByArea, Node node) {
		super(node.getTstamp().getTime(), node.getUser().getId(), nodesByArea
				.get(node.getNodeId()));
		this.node = node;
		this.nodesByArea = nodesByArea;
		this.userId = this.node.getUser().getId();
		this.itemId = this.nodesByArea.get(node.getId());
		this.tstamp = this.node.getTstamp().getTime();
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	public NodeEdit() {
		super();
	}

	public NodeEdit(Long itemId, Long userId, Long tstamp) {
		super(itemId, userId, tstamp);
		this.itemId = itemId;
		this.userId = userId;
		this.tstamp = tstamp;
	}

	public Edit getEdit() {
		return new Edit(tstamp, itemId, userId);
	}

}
