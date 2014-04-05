package com.osmrecommend.dao;

import it.unimi.dsi.fastutil.longs.LongSet;

import org.grouplens.lenskit.data.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.osmrecommend.persistence.service.UserService;

@Configurable
public class OSMRecommendUserDAO implements UserDAO {

	@Autowired
	UserService service;
	
	@Override
	public LongSet getUserIds() {
		return service.getAllUserIDs();
	}

}
