package com.sj.search.repository;


import com.sj.search.domain.UserInfoEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoEsRepository extends ElasticsearchRepository<UserInfoEs, String>{
}
