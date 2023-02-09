package com.sj.search.repository;


import com.sj.search.domain.StrategyEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StrategyEsRepository extends ElasticsearchRepository<StrategyEs, String>{
}
