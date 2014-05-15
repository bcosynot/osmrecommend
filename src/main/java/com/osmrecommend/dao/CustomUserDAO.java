package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.grouplens.lenskit.collections.LongUtils;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.service.UserService;

@Configurable
public class CustomUserDAO implements UserDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomUserDAO.class);

	@Autowired
	UserService service;

	Long2ObjectMap<User> userMap;
	ObjectList<User> allUsers;
	LongSet userIds;

	public Long2ObjectMap<User> getUserMap() {
		return userMap;
	}

	public ObjectList<User> getAllUsers() {
		return allUsers;
	}

	@Override
	public LongSet getUserIds() {
		logger.info("Fetching user ids");
		return userIds;
	}

	public CustomUserDAO(UserService userService, ObjectList<User> allUsers) {
		if (null == this.service)
			this.service = userService;

		logger.info("Fetching all users.");
		this.allUsers = allUsers;
		LongList userIdList = new LongArrayList();
		userMap = new Long2ObjectOpenHashMap<User>();
		for (User user : allUsers) {
			Long userId = user.getId();
			userMap.put(userId, user);
			userIdList.add(userId);
		}
		logger.info("Users size : " + userMap.size());
		userIds = LongUtils.packedSet(userIdList);
	}

	public User getUser(Long userId) {
		User user = null;
		if (null != (user = userMap.get(userId)))
			return user;
		else
			return null;
	}

}
