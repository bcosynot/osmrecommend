package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.osmrecommend.persistence.domain.Node;

public interface NodeRepository extends PagingAndSortingRepository<Node, Long> {

}
