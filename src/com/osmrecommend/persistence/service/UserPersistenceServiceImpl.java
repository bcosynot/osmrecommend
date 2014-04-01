package com.osmrecommend.persistence.service;

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

}
