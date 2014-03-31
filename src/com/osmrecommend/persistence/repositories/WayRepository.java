package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.osmrecommend.persistence.domain.Way;

public interface WayRepository extends PagingAndSortingRepository<Way, Long> {

}
