package com.osmrecommend.data.event.edit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.data.event.Edit;
import com.osmrecommend.persistence.domain.Way;

@Component
public class WayEdit extends Edit {

	@Autowired
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
		return way.getId();
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
