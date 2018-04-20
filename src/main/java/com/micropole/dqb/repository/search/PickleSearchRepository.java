package com.micropole.dqb.repository.search;

import com.micropole.dqb.domain.Pickle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pickle entity.
 */
public interface PickleSearchRepository extends ElasticsearchRepository<Pickle, Long> {
}
