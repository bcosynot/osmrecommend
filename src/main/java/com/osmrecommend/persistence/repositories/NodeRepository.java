package com.osmrecommend.persistence.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.osmrecommend.persistence.domain.Node;
import com.osmrecommend.persistence.domain.User;

@Repository
public interface NodeRepository extends PagingAndSortingRepository<Node, Long> {

	public Iterable<Node> findByUser(User user);
}
