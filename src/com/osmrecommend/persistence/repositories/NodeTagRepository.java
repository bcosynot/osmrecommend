package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.NodeTag;

public interface NodeTagRepository extends
		PagingAndSortingRepository<NodeTag, Node> {

}
