package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.osmrecommend.persistence.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

}
