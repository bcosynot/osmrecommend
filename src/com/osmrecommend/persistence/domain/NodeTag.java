package com.osmrecommend.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "node_tags")
public class NodeTag {

	@OneToOne
	@Column(name = "node_id")
	private Node node;
	
	@Column(name = "k")
	private String k;
	
	@Column(name = "v")
	private String v;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
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
