package com.sj.search.service.impl;

import com.sj.search.domain.TravelEs;
import com.sj.search.repository.TravelEsRepository;
import com.sj.search.service.ITravelEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TravelEsServiceImpl implements ITravelEsService {

    @Autowired
    private TravelEsRepository repository;

    @Override
    public void save(TravelEs travelEsEs) {
        //travelEsEs.setId(null);
        repository.save(travelEsEs);
    }

    @Override
    public void update(TravelEs travelEsEs) {
        repository.save(travelEsEs);
    }

    @Override
    public TravelEs get(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<TravelEs> list() {
        List<TravelEs> list = new ArrayList<>();
        Iterable<TravelEs> all = repository.findAll();
        all.forEach(a -> list.add(a));
        return list;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
