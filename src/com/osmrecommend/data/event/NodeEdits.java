package com.osmrecommend.data.event;

import com.google.common.collect.Ordering;

public final class NodeEdits {

	public static final Ordering<NodeEdit> ITEM_TIME_COMPARATOR = new Ordering<NodeEdit>() {
		
		@Override
		public int compare(NodeEdit ne1, NodeEdit ne2) {
			//TODO: implement this method
			return 0;
		}
	};
}
