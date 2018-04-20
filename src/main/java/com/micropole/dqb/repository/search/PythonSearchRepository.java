package com.micropole.dqb.repository.search;

import com.micropole.dqb.domain.Python;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Python entity.
 */
public interface PythonSearchRepository extends ElasticsearchRepository<Python, Long> {
}
