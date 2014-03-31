package com.osmrecommend.persistence.domain;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "way_tags")
public class WayTag {

	@OneToOne
	@Column(name = "way_id")
	private Way way;
	
	@Column(name = "k")
	private String k;
	
	@Column(name = "v")
	private String v;

	public Way getNode() {
		return way;
	}

	public void setNode(Way way) {
		this.way = way;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}
	
}
