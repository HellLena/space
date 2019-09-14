package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ShipRepository<T> extends PagingAndSortingRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {

}
