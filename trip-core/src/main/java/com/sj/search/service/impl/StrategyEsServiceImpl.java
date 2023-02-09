package com.sj.search.service.impl;


import com.sj.search.domain.StrategyEs;
import com.sj.search.repository.StrategyEsRepository;
import com.sj.search.service.IStrategyEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StrategyEsServiceImpl implements IStrategyEsService {

    @Autowired
    private StrategyEsRepository repository;

    @Override
    public void save(StrategyEs strategyEsEs) {
        //strategyEsEs.setId(null);
        repository.save(strategyEsEs);
    }

    @Override
    public void update(StrategyEs strategyEsEs) {
        repository.save(strategyEsEs);
    }

    @Override
    public StrategyEs get(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<StrategyEs> list() {
        List<StrategyEs> list = new ArrayList<>();
        Iterable<StrategyEs> all = repository.findAll();
        all.forEach(a -> list.add(a));
        return list;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
