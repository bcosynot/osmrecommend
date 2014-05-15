/**
 * 
 */
package com.osmrecommend.data.event;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Iterator;

import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.AbstractUserHistory;
import org.grouplens.lenskit.data.history.UserHistory;

import com.google.common.base.Predicate;
import com.osmrecommend.persistence.domain.User;

/**
 * @author vranjan
 * 
 */
public class CustomUserHistory<E extends Event> extends AbstractUserHistory<E>
		implements UserHistory<E> {

	private User user;

	private ObjectList<E> eventHistory;

	private Long userId;

	@Override
	public long getUserId() {
		Long userId = null;
		if (null != user) {
			if (null != user.getId())
			{
				user.setId(userId);
			}
			userId = user.getId();
		}
		if (null == userId)
			userId = this.userId;
		return userId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Event> UserHistory<T> filter(Class<T> type) {
		ObjectList<T> newEventHistory = new ObjectArrayList<T>();
		Iterator<E> eventHistoryIterator = eventHistory.iterator();
		while (eventHistoryIterator.hasNext()) {
			E next = eventHistoryIterator.next();
			if (next.getClass() == type) {
				newEventHistory.add((T) next);
			}
		}
		CustomUserHistory<T> newCustomUserHistory = new CustomUserHistory<T>(
				user, userId, newEventHistory);
		return newCustomUserHistory;
	}

	@Override
	public UserHistory<E> filter(Predicate<? super E> pred) {
		return this;
	}

	@Override
	public E get(int arg0) {
		return eventHistory.get(arg0);
	}

	@Override
	public int size() {
		return eventHistory.size();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ObjectList<E> getEventHistory() {
		return eventHistory;
	}

	public void setEventHistory(ObjectList<E> eventHistory) {
		this.eventHistory = eventHistory;
	}

	public CustomUserHistory() {
		super();
	}

	public CustomUserHistory(User user) {
		super();
		this.user = user;
		this.eventHistory = new ObjectArrayList<E>();
	}

	public CustomUserHistory(User user, Long userId, ObjectList<E> eventHistory) {
		super();
		this.user = user;
		this.eventHistory = eventHistory;
		this.userId = userId;
		this.user.setId(userId);
	}

}
