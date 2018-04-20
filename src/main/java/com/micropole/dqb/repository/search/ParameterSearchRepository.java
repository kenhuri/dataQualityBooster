package com.micropole.dqb.repository.search;

import com.micropole.dqb.domain.Parameter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Parameter entity.
 */
public interface ParameterSearchRepository extends ElasticsearchRepository<Parameter, Long> {
}
