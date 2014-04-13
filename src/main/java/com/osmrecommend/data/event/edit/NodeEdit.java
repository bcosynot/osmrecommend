package com.osmrecommend.data.event.edit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.data.event.Edit;
import com.osmrecommend.persistence.domain.Node;

@Component
public class NodeEdit extends Edit {
	
	@Autowired
	Node node;

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
		return node.getTstamp().getTime();
	}

	/**
	 * @param node
	 */
	public NodeEdit(Node node) {
		super();
		this.node = node;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	public NodeEdit() {
		super();
	}

}
