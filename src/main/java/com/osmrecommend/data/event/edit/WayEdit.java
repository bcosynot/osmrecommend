package com.osmrecommend.data.event.edit;

import com.osmrecommend.data.event.Edit;
import com.osmrecommend.persistence.domain.Way;

public class WayEdit extends Edit {

	private Way way;
	
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
		return way.getUser().getId();
	}

	@Override
	public long getItemId() {
		return way.getWayId();
	}

	@Override
	public long getTimestamp() {
		return way.getTimestamp().getTime();
	}
	
	public WayEdit() {
		super();
	}
	
	public WayEdit(Way way) {
		super();
		this.way = way;
	}
	

}
