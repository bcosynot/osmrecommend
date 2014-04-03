package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.domain.WayTag;

@Repository
public interface WayTagRepository extends
		PagingAndSortingRepository<WayTag, Way> {

}
