/**
 * 
 */
package com.osmrecommend.data.event.dao;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.UserHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osmrecommend.dao.CustomUserDAO;
import com.osmrecommend.data.event.CustomUserHistory;
import com.osmrecommend.data.event.Edit;
import com.osmrecommend.data.event.edit.NodeEdit;
import com.osmrecommend.data.event.edit.WayEdit;
import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.service.NodeService;
import com.osmrecommend.persistence.service.UserService;
import com.osmrecommend.persistence.service.WayService;

/**
 * @author vranjan
 * 
 */
public class CustomUserEventDAO implements UserEventDAO {

	public static final Logger logger = LoggerFactory
			.getLogger(CustomUserEventDAO.class);

	NodeService nodeService;

	WayService wayService;

	UserService userService;

	Long2LongMap nodesByArea;
	Long2LongMap waysByArea;

	CustomUserDAO userDAO;
	EditDAO editDAO;

	public CustomUserEventDAO() {
		super();
	}

	public CustomUserEventDAO(NodeService nodeService, WayService wayService,
			UserService userService, Long2LongMap nodesByArea,
			Long2LongMap waysByArea, CustomUserDAO customUserDAO,
			EditDAO editDAO) {
		super();
		if (null != nodeService)
			this.nodeService = nodeService;
		if (null != wayService)
			this.wayService = wayService;
		if (null != userService)
			this.userService = userService;
		this.nodesByArea = nodesByArea;
		this.waysByArea = waysByArea;
		this.userDAO = customUserDAO;
		this.editDAO = editDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grouplens.lenskit.data.dao.UserEventDAO#streamEventsByUser()
	 */
	@Override
	public Cursor<UserHistory<Event>> streamEventsByUser() {
		return streamEventsByUser(Event.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.grouplens.lenskit.data.dao.UserEventDAO#streamEventsByUser(java.lang
	 * .Class)
	 */
	@Override
	public <E extends Event> Cursor<UserHistory<E>> streamEventsByUser(
			Class<E> type) {
		ObjectList<UserHistory<E>> userHistories = new ObjectArrayList<UserHistory<E>>();

		LongSet userIds = userDAO.getUserIds();
		logger.info("Getting user histories for " + userIds.size() + " users.");
		for (Long user : userIds) {
			ObjectList<E> allEdits = new ObjectArrayList<E>();

			User userObject = null;
			if (null == (userObject = userDAO.getUser(user))) {
				continue;
			}

			if (null != userObject) {

				if (type == WayEdit.class || type == Edit.class
						|| type == Event.class) {

					// Get all ways
					Iterable<Way> allWaysForUser = null;
					ObjectList<WayEdit> wayEdits = null;
					if (null == (wayEdits = editDAO.getWaysForUser(user))) {
						/*try {
							allWaysForUser = wayService
									.getAllForUser(userObject);
						} catch (Exception e) {
							logger.info("Problem in fetching ways for user "
									+ user);
							e.printStackTrace();
						}
						if (null != allWaysForUser) {
							if (null == wayEdits) {
								wayEdits = new ObjectArrayList<WayEdit>();
							}
							for (Way way : allWaysForUser) {
								Long itemId = null;
								if (null == (itemId = waysByArea.get(way
										.getWayId()))) {
									continue;
								}
								Edit edit = new Edit(way, itemId, user);
								allEdits.add((E) edit);
								wayEdits.add(edit.getWayEdit());
							}
						}*/
					} else {
						for (WayEdit wayEdit : wayEdits) {
							allEdits.add((E) wayEdit.getEdit());
						}
					}

				}

				if (type == NodeEdit.class || type == Edit.class
						|| type == Event.class) {

					// Get all nodes
					Iterable<Node> allNodesForUser = null;
					ObjectList<NodeEdit> nodeEdits = null;
					if (null == (nodeEdits = editDAO.getNodesForUser(user))) {
						/*try {
							allNodesForUser = nodeService
									.getAllForUser(userObject);
						} catch (Exception e) {
							logger.info("Problem in fetching nodes for user "
									+ user);
							e.printStackTrace();
						}
						if (null != allNodesForUser) {
							if (null == nodeEdits) {
								nodeEdits = new ObjectArrayList<NodeEdit>();
							}
							for (Node node : allNodesForUser) {
								Long itemId = null;
								if (null == (itemId = nodesByArea.get(node
										.getNodeId()))) {
									continue;
								}
								Edit edit = new Edit(node, itemId, user);
								allEdits.add((E) edit);
								nodeEdits.add(edit.getNodeEdit());
							}
						}*/
					} else {
						for (NodeEdit nodeEdit : nodeEdits) {
							allEdits.add((E) nodeEdit.getEdit());
						}
					}

				}
			}
			userHistories.add(new CustomUserHistory<E>(userObject, user,
					allEdits));
		}
		logger.info("Total histories added: " + userHistories.size());

		return Cursors.wrap(userHistories);
	}

	public Cursor<CustomUserHistory<Edit>> streamCustomEventsByUser() {

		ObjectList<CustomUserHistory<Edit>> userHistories = new ObjectArrayList<CustomUserHistory<Edit>>();

		LongSet userIds = userDAO.getUserIds();

		logger.info("Getting user histories for " + userIds.size() + " users.");
		for (Long user : userIds) {

			ObjectList<Edit> allEdits = new ObjectArrayList<Edit>();

			User userObject = null;
			if (null == (userObject = userDAO.getUser(user))) {
				continue;
			}

			if (null != userObject) {

				// Get all ways
				Iterable<Way> allWaysForUser = null;
				ObjectList<WayEdit> wayEdits = null;
				if (null == (wayEdits = editDAO.getWaysForUser(user))) {
					/*try {
						allWaysForUser = wayService.getAllForUser(userObject);
					} catch (Exception e) {
						logger.info("Problem in fetching ways for user " + user);
						e.printStackTrace();
					}
					if (null != allWaysForUser) {
						if (null == wayEdits) {
							wayEdits = new ObjectArrayList<WayEdit>();
						}
						for (Way way : allWaysForUser) {
							Long itemId = null;
							if (null == (itemId = waysByArea
									.get(way.getWayId()))) {
								continue;
							}
							Edit edit = new Edit(way, itemId, user);
							allEdits.add(edit);
							wayEdits.add(edit.getWayEdit());
						}
					}*/
				} else {
					for (WayEdit wayEdit : wayEdits) {
						allEdits.add(wayEdit.getEdit());
					}
				}

				// Get all nodes
				Iterable<Node> allNodesForUser = null;
				ObjectList<NodeEdit> nodeEdits = null;
				if (null == (nodeEdits = editDAO.getNodesForUser(user))) {
					/*try {
						allNodesForUser = nodeService.getAllForUser(userObject);
					} catch (Exception e) {
						logger.info("Problem in fetching nodes for user "
								+ user);
						e.printStackTrace();
					}
					if (null != allNodesForUser) {
						if (null == nodeEdits) {
							nodeEdits = new ObjectArrayList<NodeEdit>();
						}
						for (Node node : allNodesForUser) {
							Long itemId = null;
							if (null == (itemId = nodesByArea.get(node
									.getNodeId()))) {
								continue;
							}
							Edit edit = new Edit(node, itemId, user);
							allEdits.add(edit);
							nodeEdits.add(edit.getNodeEdit());
						}
					}*/

				} else {
					for (NodeEdit nodeEdit : nodeEdits) {
						allEdits.add(nodeEdit.getEdit());
					}
				}
			}
			userHistories.add(new CustomUserHistory<Edit>(userObject, user,
					allEdits));
		}
		logger.info("Total histories added: " + userHistories.size());

		return Cursors.wrap(userHistories);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grouplens.lenskit.data.dao.UserEventDAO#getEventsForUser(long)
	 */
	@Override
	public UserHistory<Event> getEventsForUser(long user) {
		return getEventsForUser(user, Event.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grouplens.lenskit.data.dao.UserEventDAO#getEventsForUser(long,
	 * java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E extends Event> UserHistory<E> getEventsForUser(long user,
			Class<E> type) {
		/*
		 * logger.info("stream events of type " + type.toString() + " for user:"
		 * + user);
		 */
		ObjectList<E> allEdits = new ObjectArrayList<E>();

		User userObject = null;
		if (null == (userObject = userDAO.getUser(user))) {
			return null;
		}

		if (null != userObject) {

			if (type == WayEdit.class || type == Edit.class
					|| type == Event.class) {

				// Get all ways
				Iterable<Way> allWaysForUser = null;
				ObjectList<WayEdit> wayEdits = null;
				if (null == (wayEdits = editDAO.getWaysForUser(user))) {
					/*try {
						allWaysForUser = wayService.getAllForUser(userObject);
					} catch (Exception e) {
						logger.info("Problem in fetching ways for user " + user);
						e.printStackTrace();
					}
					if (null != allWaysForUser) {
						if (null == wayEdits) {
							wayEdits = new ObjectArrayList<WayEdit>();
						}
						for (Way way : allWaysForUser) {
							Long itemId = null;
							if (null == (itemId = waysByArea
									.get(way.getWayId()))) {
								continue;
							}
							Edit edit = new Edit(way, itemId, user);
							allEdits.add((E) edit);
							wayEdits.add(edit.getWayEdit());
						}
					}*/
				} else {
					for (WayEdit wayEdit : wayEdits) {
						allEdits.add((E) wayEdit.getEdit());
					}
				}
			}

			if (type == NodeEdit.class || type == Edit.class
					|| type == Event.class) {

				// Get all nodes
				Iterable<Node> allNodesForUser = null;
				ObjectList<NodeEdit> nodeEdits = null;
				if (null == (nodeEdits = editDAO.getNodesForUser(user))) {
					/*try {
						allNodesForUser = nodeService.getAllForUser(userObject);
					} catch (Exception e) {
						logger.info("Problem in fetching nodes for user "
								+ user);
						e.printStackTrace();
					}
					if (null != allNodesForUser) {
						if (null == nodeEdits) {
							nodeEdits = new ObjectArrayList<NodeEdit>();
						}
						for (Node node : allNodesForUser) {
							Long itemId = null;
							if (null == (itemId = nodesByArea.get(node
									.getNodeId()))) {
								continue;
							}
							Edit edit = new Edit(node, itemId, user);
							allEdits.add((E) edit);
							nodeEdits.add(edit.getNodeEdit());
						}
					}*/
				} else {
					for (NodeEdit nodeEdit : nodeEdits) {
						allEdits.add((E) nodeEdit.getEdit());
					}
				}

			}
		}

		CustomUserHistory<E> customUserHistory = new CustomUserHistory<E>(
				userObject, user, allEdits);

		return customUserHistory;

	}

	public CustomUserHistory<Edit> getCustomEventsForUser(long user) {
		/*
		 * logger.info("stream events of type " + type.toString() + " for user:"
		 * + user);
		 */
		ObjectList<Edit> allEdits = new ObjectArrayList<Edit>();

		User userObject = null;
		if (null == (userObject = userDAO.getUser(user))) {
			return null;
		}

		if (null != userObject) {

			// Get all ways
			Iterable<Way> allWaysForUser = null;
			ObjectList<WayEdit> wayEdits = null;
			if (null == (wayEdits = editDAO.getWaysForUser(user))) {
				/*try {
					allWaysForUser = wayService.getAllForUser(userObject);
				} catch (Exception e) {
					logger.info("Problem in fetching ways for user " + user);
					e.printStackTrace();
				}
				if (null != allWaysForUser) {
					if (null == wayEdits) {
						wayEdits = new ObjectArrayList<WayEdit>();
					}
					for (Way way : allWaysForUser) {
						Long itemId = null;
						if (null == (itemId = waysByArea.get(way.getWayId()))) {
							continue;
						}
						Edit edit = new Edit(way, itemId, user);
						allEdits.add(edit);
					}
				}*/
			} else {
				for (WayEdit wayEdit : wayEdits) {
					allEdits.add(wayEdit.getEdit());
				}
			}

			// Get all nodes
			Iterable<Node> allNodesForUser = null;
			ObjectList<NodeEdit> nodeEdits = null;
			if (null == (nodeEdits = editDAO.getNodesForUser(user))) {
				/*try {
					allNodesForUser = nodeService.getAllForUser(userObject);
				} catch (Exception e) {
					logger.info("Problem in fetching nodes for user " + user);
					e.printStackTrace();
				}
				if (null != allNodesForUser) {
					if (null == nodeEdits) {
						nodeEdits = new ObjectArrayList<NodeEdit>();
					}
					for (Node node : allNodesForUser) {
						Long itemId = null;
						if (null == (itemId = nodesByArea.get(node.getNodeId()))) {
							continue;
						}
						Edit edit = new Edit(node, itemId, user);
						allEdits.add(edit);
					}
				}*/
			} else {
				for (NodeEdit nodeEdit : nodeEdits) {
					allEdits.add(nodeEdit.getEdit());
				}
			}

		}

		CustomUserHistory<Edit> customUserHistory = new CustomUserHistory<Edit>(
				userObject, user, allEdits);

		return customUserHistory;

	}

}
