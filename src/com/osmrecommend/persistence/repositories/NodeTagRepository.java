package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.NodeTag;

@Repository
public interface NodeTagRepository extends
		PagingAndSortingRepository<NodeTag, Node> {

}
