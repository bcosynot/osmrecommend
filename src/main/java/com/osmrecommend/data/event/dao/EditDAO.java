package com.osmrecommend.data.event.dao;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Comparator;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osmrecommend.data.event.Edit;
import com.osmrecommend.data.event.edit.NodeEdit;
import com.osmrecommend.data.event.edit.WayEdit;
import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.service.NodeService;
import com.osmrecommend.persistence.service.WayService;

public class EditDAO implements EventDAO {

	private static final Logger logger = LoggerFactory.getLogger(EditDAO.class);

	NodeService nodeService;

	WayService wayService;

	Long2LongMap nodesByArea;
	Long2LongMap waysByArea;

	Long2ObjectMap<ObjectList<NodeEdit>> nodeMap;
	Long2ObjectMap<ObjectList<WayEdit>> wayMap;

	Thread threadToFetchAllNodes;
	Thread threadToFetchAllWays;

	private Iterable<Node> allNodes;

	private Iterable<Way> allWays;

	private ObjectList<Edit> allEdits;
	private ObjectList<NodeEdit> allNodeEdits;
	private ObjectList<WayEdit> allWayEdits;
	private ObjectList<Event> allEvents;

	private boolean nodeEditsAdded;

	private boolean wayEditsAdded;

	public EditDAO() {
		super();
	}

	public EditDAO(NodeService nodeServiceArg, WayService wayServiceArg,
			Long2LongMap nodesByArea, Long2LongMap waysByArea) {
		this.nodeService = nodeServiceArg;
		this.wayService = wayServiceArg;
		this.nodesByArea = nodesByArea;
		this.waysByArea = waysByArea;

	}

	public EditDAO(NodeService nodeServiceArg, WayService wayServiceArg) {
		this.nodeService = nodeServiceArg;
		this.wayService = wayServiceArg;
	}

	public void setNodesByArea(Long2LongMap nodesByArea) {
		this.nodesByArea = nodesByArea;
	}

	public void setWaysByArea(Long2LongMap waysByArea) {
		this.waysByArea = waysByArea;
	}

	public void nodeInit() {
		if (null == threadToFetchAllNodes) {
			threadToFetchAllNodes = new Thread() {
				@Override
				public void run() {
					super.run();
					if (allNodes == null) {
						try {
							Long startTime = System.currentTimeMillis();
							allNodes = nodeService.getAllNodes();
							Long endTime = System.currentTimeMillis();
							logger.info("Nodes fetched in "
									+ (endTime - startTime) + "ms");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
		}
		if (!threadToFetchAllNodes.isAlive() && null == allNodes) {
			logger.info("fetching nodes..");
			threadToFetchAllNodes.start();
		}
	}

	@Override
	public Cursor<Event> streamEvents() {

		logger.info("stream all events");

		if (null == allEvents) {
			allEvents = new ObjectArrayList<Event>();
		}

		if (allEvents.size() > 10) {
			return Cursors.wrap(allEvents);
		}

		nodeInit();
		wayInit();

		fetchWays();

		fetchNodes();

		return Cursors.wrap(allEvents);
	}

	private void fetchNodes() {
		try {
			if (null == allNodes) {
				logger.info("Waiting for node fetching thread to finish");
				threadToFetchAllNodes.join();
				logger.info("Nodes fetching thread finished");
			}
			if (null != allNodes) {
				if (null == allNodeEdits) {
					allNodeEdits = new ObjectArrayList<NodeEdit>();
					if (null == nodeMap) {
						nodeMap = new Long2ObjectOpenHashMap<ObjectList<NodeEdit>>();
					}
					for (Node node : allNodes) {
						if (null != node) {
							if (null != node.getTstamp()) {
								Long itemId = null;
								if (null == (itemId = nodesByArea.get(node
										.getId()))) {
									continue;
								}
								Edit edit = new Edit(node, itemId, node
										.getUser().getId());
								NodeEdit nodeEdit = edit.getNodeEdit();
								allNodeEdits.add(nodeEdit);
								Long userId = node.getUser().getId();
								ObjectList<NodeEdit> nodeListForUser = null;
								if (null == (nodeListForUser = nodeMap
										.get(userId))) {
									nodeListForUser = new ObjectArrayList<NodeEdit>();
								}
								nodeListForUser.add(nodeEdit);
								nodeMap.put(userId, nodeListForUser);
							} else {
								logger.info("node tstamp is null "
										+ node.getId());
							}
						}
					}
				}
				if (null != allNodeEdits) {
					if (null == allEvents) {
						allEvents = new ObjectArrayList<Event>();
					}
					if (!nodeEditsAdded) {
						allEvents.addAll(allNodeEdits);
						nodeEditsAdded = true;
					}
				} else {
					logger.info("allNodeEdits is null");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void fetchWays() {

		try {
			if (null == allWays) {
				logger.info("Waiting for ways fetching thread to finish");
				threadToFetchAllWays.join();
				logger.info("Ways fetching thread finished");
			}
			if (null != allWays) {
				if (null == allWayEdits) {
					allWayEdits = new ObjectArrayList<WayEdit>();
					if (null == wayMap) {
						wayMap = new Long2ObjectOpenHashMap<ObjectList<WayEdit>>();
					}
					for (Way way : allWays) {
						if (null != way) {
							if (null != way.getTimestamp()) {
								Long itemId = null;
								if (null == (itemId = waysByArea.get(way
										.getId()))) {
									continue;
								}
								Edit edit = new Edit(way, itemId, way.getUser()
										.getId());
								WayEdit wayEdit = edit.getWayEdit();
								allWayEdits.add(wayEdit);
								Long userId = way.getUser().getId();
								ObjectList<WayEdit> wayListForUser = null;
								if (null == (wayListForUser = wayMap
										.get(userId))) {
									wayListForUser = new ObjectArrayList<WayEdit>();
								}
								wayListForUser.add(wayEdit);
								wayMap.put(userId, wayListForUser);
							} else {
								logger.info("way tstamp is null " + way.getId());
							}
						}
					}
				}
				if (null != allWayEdits) {
					if (null == allEvents) {
						allEvents = new ObjectArrayList<Event>();
					}
					if (!wayEditsAdded) {
						allEvents.addAll(allWayEdits);
						wayEditsAdded = true;
					}
				} else {
					logger.info("allWayEdits null");
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void wayInit() {
		if (null == threadToFetchAllWays) {
			threadToFetchAllWays = new Thread() {
				@Override
				public void run() {
					super.run();
					if (allWays == null) {
						try {
							allWays = wayService.getAllWays();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
		}
		if (!threadToFetchAllWays.isAlive() && null == allWays) {
			logger.info("fetching ways...");
			threadToFetchAllWays.start();
		}
	}

	@Override
	public <E extends Event> Cursor<E> streamEvents(Class<E> type) {
		logger.info("stream events of type " + type.toString());
		return streamEvents(type, SortOrder.ANY);
	}

	@Override
	public <E extends Event> Cursor<E> streamEvents(Class<E> type,
			SortOrder order) {

		logger.info("stream events of type " + type.toString() + " in order "
				+ order.toString());

		if (null == allEdits) {
			allEdits = new ObjectArrayList<Edit>();
		}

		if (allEdits.size() < 10) {

			if (type == NodeEdit.class || type == Edit.class
					|| type == Event.class) {
				logger.info("type is NodeEdit");
				// Get all nodes
				logger.info("fetching nodes..");

				threadToFetchAllNodes = new Thread() {
					@Override
					public void run() {
						super.run();
						if (allNodes == null) {
							try {
								allNodes = nodeService.getAllNodes();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				threadToFetchAllNodes.start();
			}

			if (type == WayEdit.class || type == Edit.class
					|| type == Event.class) {
				// Get all ways
				threadToFetchAllWays = new Thread() {
					@Override
					public void run() {
						super.run();
						if (allWays == null) {
							try {
								allWays = wayService.getAllWays();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				threadToFetchAllWays.start();
				try {
					if (null == allWays) {
						if (null == threadToFetchAllWays) {
							wayInit();
						}
						logger.info("Waiting for ways fetching thread to finish");
						threadToFetchAllWays.join();
					}
					if (null != allWays) {
						if (null == allWayEdits) {
							allWayEdits = new ObjectArrayList<WayEdit>();
							for (Way way : allWays) {
								Long itemId = null;
								if (null == (itemId = waysByArea.get(way
										.getWayId()))) {
									continue;
								}
								Edit edit = new Edit(way, itemId, way.getUser()
										.getId());
								allEdits.add(edit);
								allWayEdits.add(edit.getWayEdit());
							}
						}
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			if (type == NodeEdit.class || type == Edit.class
					|| type == Event.class) {
				try {
					if (null == allNodes) {
						if (null == threadToFetchAllNodes) {
							nodeInit();
						}
						logger.info("Waiting for node fetching thread to finish");
						threadToFetchAllNodes.join();
					}
					if (null != allNodes) {
						if (null == allNodeEdits) {
							allNodeEdits = new ObjectArrayList<NodeEdit>();
							for (Node node : allNodes) {
								Long itemId = null;
								if (null == (itemId = nodesByArea.get(node
										.getNodeId()))) {
									continue;
								}
								Edit edit = new Edit(node, itemId, node.getId());
								allEdits.add(edit);
								allNodeEdits.add(edit.getNodeEdit());
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		Comparator<Event> comp = order.getEventComparator();
		Cursor<Edit> cursor = Cursors.wrap(allEdits);

		if (comp == null) {
			return (Cursor<E>) cursor;
		} else {
			return (Cursor<E>) Cursors.sort(cursor, comp);
		}

	}

	public NodeService getNodeService() {
		return nodeService;
	}

	public WayService getWayService() {
		return wayService;
	}

	public Long2LongMap getNodesByArea() {
		return nodesByArea;
	}

	public Long2LongMap getWaysByArea() {
		return waysByArea;
	}

	public Long2ObjectMap<ObjectList<NodeEdit>> getNodeMap() {
		return nodeMap;
	}

	public Long2ObjectMap<ObjectList<WayEdit>> getWayMap() {
		return wayMap;
	}

	public Iterable<Node> getAllNodes() {
		if (!nodeEditsAdded)
			fetchNodes();
		return allNodes;
	}

	public Iterable<Way> getAllWays() {
		if (!wayEditsAdded)
			fetchWays();
		return allWays;
	}

	public ObjectList<Edit> getAllEdits() {
		return allEdits;
	}

	public ObjectList<Event> getAllEvents() {
		return allEvents;
	}

	public ObjectList<NodeEdit> getNodesForUser(Long userId) {
		if (!nodeEditsAdded)
			fetchNodes();
		if (null != nodeMap) {
			ObjectList<NodeEdit> nodes = null;
			if (null != (nodes = nodeMap.get(userId))) {
				return nodes;
			}
		}
		return null;
	}

	public ObjectList<WayEdit> getWaysForUser(Long userId) {
		if (!wayEditsAdded)
			fetchWays();
		if (null != wayMap) {
			ObjectList<WayEdit> ways = null;
			if (null != (ways = wayMap.get(userId))) {
				return ways;
			}
		}
		return null;
	}
	/**
	 * 
	 */
	/*
	 * public EditDAO() { super();
	 * 
	 * nodeService = new NodePersistenceServiceImpl(); wayService = new
	 * WayPersistenceServiceImpl();
	 * 
	 * }
	 */

}
