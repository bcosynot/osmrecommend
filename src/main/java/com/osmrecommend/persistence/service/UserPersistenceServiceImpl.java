package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.grouplens.lenskit.collections.LongUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.repositories.UserRepository;

@Component
public class UserPersistenceServiceImpl implements UserService {

	@Autowired
	UserRepository repo;

	@Override
	public User getUser(Long id) throws Exception {
		User user = null;
		if (null == (user = repo.findOne(id))) {
			throw new Exception("User not found with ID : " + id);
		} else {
			return user;
		}
	}

	@Override
	public LongSet getAllUserIDs() {

		ObjectList<Long> userIds = new ObjectArrayList<Long>();

		for (User user : repo.findAll()) {

			userIds.add(user.getId().longValue());

		}

		return LongUtils.packedSet(userIds);
	}

	@Override
	public ObjectList<User> getAllUsers() {
		
		ObjectList<User> allUsers = new ObjectArrayList<User>();
		
		for(User user : repo.findAll()) {
			allUsers.add(user);
		}
		
		return allUsers;
	}

}
