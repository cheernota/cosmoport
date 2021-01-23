package com.space.service;


import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Ship getShip(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    public Ship saveShip (Ship ship){
        return shipRepository.save(ship);
    }

    public Ship saveAndFlushShip (Ship ship){
        return shipRepository.saveAndFlush(ship);
    }

    public Boolean isExist(Long id) {
        return shipRepository.existsById(id);
    }

    public void deleteShip (Long id){
        shipRepository.deleteById(id);
    }

    public Page<Ship> getAllShips(Specification<Ship> spec, Pageable pageable) { return shipRepository.findAll(spec, pageable); }

    public Integer getShipsCount(Specification<Ship> spec) {
        return shipRepository.findAll(spec).size();
    }
}
