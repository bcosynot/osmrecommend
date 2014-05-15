package com.osmrecommend.data.event;

import it.unimi.dsi.fastutil.longs.Long2LongMap;

import org.grouplens.lenskit.data.event.Event;

import com.osmrecommend.data.event.edit.NodeEdit;
import com.osmrecommend.data.event.edit.WayEdit;
import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.Way;

public class Edit implements Event {

	protected Node node;

	protected Way way;

	protected Long tstamp;

	protected Long itemId;
	
	protected Long userId;

	Long2LongMap nodesByArea;

	public Edit() {
	}

	public Edit(Node node, Long itemId, Long userId) {
		this.node = node;
		this.tstamp = node.getTstamp().getTime();
		this.itemId = itemId;
		this.userId = userId;
	}

	public Edit(Way way, Long itemId, Long userId) {
		this.way = way;
		this.tstamp = way.getTimestamp().getTime();
		this.itemId = itemId;
		this.userId = userId;
	}
	
	public Edit(Long tstamp, Long itemId, Long userId) {
		this.tstamp = tstamp;
		this.itemId = itemId;
		this.userId = userId;
	}

	public Edit(Long tstamp) {
		this.tstamp = tstamp;
	}

	@Override
	public long getTimestamp() {
		return tstamp;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	@Override
	public long getItemId() {
		return itemId;
	}
	
	public NodeEdit getNodeEdit() {
		return new NodeEdit(itemId, userId, tstamp);
	}
	
	public WayEdit getWayEdit() {
		return new WayEdit(itemId, userId, tstamp);
	}

}
