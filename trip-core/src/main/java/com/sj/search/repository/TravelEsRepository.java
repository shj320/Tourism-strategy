package com.sj.search.repository;

import com.sj.search.domain.TravelEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TravelEsRepository extends ElasticsearchRepository<TravelEs, String>{
}
