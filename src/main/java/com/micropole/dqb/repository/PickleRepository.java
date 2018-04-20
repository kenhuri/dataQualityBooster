package com.micropole.dqb.repository;

import com.micropole.dqb.domain.Pickle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pickle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PickleRepository extends JpaRepository<Pickle, Long> {

}
