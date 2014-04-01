package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

}
