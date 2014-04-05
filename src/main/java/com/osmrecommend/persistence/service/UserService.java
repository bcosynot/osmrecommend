package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;

import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.User;

@Service
public interface UserService {

	public User getUser(Long id);
	public LongSet getAllUserIDs();
}
