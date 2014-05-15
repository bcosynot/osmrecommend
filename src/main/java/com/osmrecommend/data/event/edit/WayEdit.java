package com.osmrecommend.data.event.edit;

import it.unimi.dsi.fastutil.longs.Long2LongMap;

import com.osmrecommend.data.event.Edit;
import com.osmrecommend.persistence.domain.Way;

public class WayEdit extends Edit {

	Way way;
	
	Long itemId;
	
	Long userId;
	
	Long tstamp;
	
	Long2LongMap waysByArea;
	
	/**
	 * @return the way
	 */
	public Way getWay() {
		return way;
	}

	/**
	 * @param way the way to set
	 */
	public void setWay(Way way) {
		this.way = way;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	@Override
	public long getItemId() {
		return itemId;
	}

	@Override
	public long getTimestamp() {
		return tstamp;
	}
	
	public WayEdit() {
		super();
	}
	
	
	public WayEdit(Long2LongMap waysByArea, Way way) {
		super(way.getTimestamp().getTime(), way.getUser().getId(), waysByArea.get(way.getWayId()));
		this.way = way;
		this.waysByArea = waysByArea;
		this.userId = way.getUser().getId();
		this.itemId = this.waysByArea.get(this.way.getId());
		this.tstamp = this.way.getTimestamp().getTime();
	}
	
	public WayEdit(Long itemId, Long userId, Long tstamp) {
		super(tstamp, itemId, userId);
		this.itemId = itemId;
		this.userId = userId;
		this.tstamp = tstamp;
	}
	
	public Edit getEdit() {
		return new Edit(tstamp, itemId, userId);
	}

}
