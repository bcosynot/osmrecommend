package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.User;

@Service
public interface UserService {

	public User getUser(Long id) throws Exception;
	public LongSet getAllUserIDs();
	public ObjectList<User> getAllUsers();
}
