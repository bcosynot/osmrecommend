package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Way;

@Repository
public interface WayRepository extends PagingAndSortingRepository<Way, Long> {

}
