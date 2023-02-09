package com.sj.search.repository;


import com.sj.search.domain.DestinationEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DestinationEsRepository extends ElasticsearchRepository<DestinationEs, String>{
}
