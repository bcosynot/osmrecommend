package com.osmrecommend.persistence.repositories;

import it.unimi.dsi.fastutil.objects.ObjectList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	@Query("SELECT u.id FROM User u")
	public ObjectList<Long> findAllUserIds();
	
}
