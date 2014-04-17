package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;

import org.grouplens.lenskit.data.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.osmrecommend.persistence.service.UserService;

@Configurable
public class CustomUserDAO implements UserDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDAO.class);

	@Autowired
	UserService service;
	
	@Override
	public LongSet getUserIds() {
		logger.info("inside getUserIds");
		return service.getAllUserIDs();
	}

}
