package com.micropole.dqb.repository.search;

import com.micropole.dqb.domain.Context;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Context entity.
 */
public interface ContextSearchRepository extends ElasticsearchRepository<Context, Long> {
}
