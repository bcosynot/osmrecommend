package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;

public interface NodeRepository extends PagingAndSortingRepository<Node, Long> {

	public Iterable<Node> findByUser(User user);
}
