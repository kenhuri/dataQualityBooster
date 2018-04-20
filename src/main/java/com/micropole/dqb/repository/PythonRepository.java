package com.micropole.dqb.repository;

import com.micropole.dqb.domain.Python;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Python entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PythonRepository extends JpaRepository<Python, Long> {

}
