package com.osmrecommend.persistence.service;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.List;

import org.grouplens.lenskit.collections.LongUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osmrecommend.persistence.domain.User;
import com.osmrecommend.persistence.repositories.UserRepository;

@Service
public class UserPersistenceServiceImpl implements UserService {

	@Autowired
	UserRepository repo;
	
	@Override
	public User getUser(Long id) {
		return repo.findOne(id);
	}
	
	@Override
	public LongSet getAllUserIDs() {
		
		List<Long> userIds = new ArrayList<Long>();
		
		for(User user : repo.findAll()) {
			
			userIds.add(user.getId());
			
		}
		
		
		return LongUtils.packedSet(userIds);
	}

}
