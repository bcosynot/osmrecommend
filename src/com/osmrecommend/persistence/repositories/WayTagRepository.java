package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.osmrecommend.persistence.domain.Way;
import com.osmrecommend.persistence.domain.WayTag;

public interface WayTagRepository extends
		PagingAndSortingRepository<WayTag, Way> {

}
