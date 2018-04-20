package com.micropole.dqb.repository.search;

import com.micropole.dqb.domain.Execution;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Execution entity.
 */
public interface ExecutionSearchRepository extends ElasticsearchRepository<Execution, Long> {
}
