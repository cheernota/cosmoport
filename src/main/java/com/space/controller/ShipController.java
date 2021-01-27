package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.utils.BadRequestException;
import com.space.utils.NotFoundException;
import com.space.utils.ShipSpecifications;
import com.space.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {
    private final ShipService shipService;
    private final Validator validator = new Validator();

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    //Get Ship`s list
    @GetMapping("/ships")
    @ResponseStatus(HttpStatus.OK)
    public List<Ship> getShipList(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "order", required = false, defaultValue = "ID") ShipOrder order,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "3") Integer pageSize
            ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        ShipSpecifications spec = new ShipSpecifications();
        return shipService.getAllShips(Specification.where(spec.selectByName(name))
                .and(spec.selectByPlanet(planet))
                .and(spec.selectByType(shipType))
                .and(spec.selectByDate(after, before))
                .and(spec.selectByUsed(isUsed))
                .and(spec.selectBySpeed(minSpeed, maxSpeed))
                .and(spec.selectByCrewSize(minCrewSize, maxCrewSize))
                .and(spec.selectByRating(minRating, maxRating)), pageable).getContent();
    }

    //Count Ship`s
    @GetMapping("/ships/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getShipCount(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating
    ) {
        ShipSpecifications spec = new ShipSpecifications();
        return shipService.getShipsCount((Specification.where(spec.selectByName(name))
                .and(spec.selectByPlanet(planet))
                .and(spec.selectByType(shipType))
                .and(spec.selectByDate(after, before))
                .and(spec.selectByUsed(isUsed))
                .and(spec.selectBySpeed(minSpeed, maxSpeed))
                .and(spec.selectByCrewSize(minCrewSize, maxCrewSize))
                .and(spec.selectByRating(minRating, maxRating))));
    }

    //Get Ship
    @GetMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Ship getShip(@PathVariable String id) {
        Long longId = validator.getValidId(id);
        if (shipService.isExist(longId)) {
            return shipService.getShip(longId);
        }
        else throw new NotFoundException("ship`s not found");
    }

    //Create Ship
    @PostMapping("/ships")
    @ResponseStatus(HttpStatus.OK)
    public Ship createShip(@RequestBody Ship ship) {
        if (ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null
                || ship.getProdDate() == null || ship.getSpeed() == null || ship.getCrewSize() == null) {
            throw new BadRequestException("wrong number of mandatory parameters");
        }

        validator.checkName(ship.getName());
        validator.checkPlanet(ship.getPlanet());
        validator.checkSpeed(ship.getSpeed());
        validator.checkCrewSize(ship.getCrewSize());
        validator.checkProdDate(ship.getProdDate());

        if (ship.isUsed() == null) {
            ship.setUsed(false);
        }

        ship.setRating(validator.calcRating(ship.getSpeed(), ship.isUsed(), ship.getProdDate()));
        return shipService.saveShip(ship);
    }

    //Update Ship
    @PostMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Ship updateShip(@PathVariable String id, @RequestBody Ship updShip) {
        Long longId = validator.getValidId(id);
        if (!shipService.isExist(longId)) {
            throw new NotFoundException("ship`s not found");
        }
        Ship ship = shipService.getShip(longId);

        if (updShip.getName() != null) {
            validator.checkName(updShip.getName());
            ship.setName(updShip.getName());
        }
        if (updShip.getPlanet() != null) {
            validator.checkPlanet(updShip.getPlanet());
            ship.setPlanet(updShip.getPlanet());
        }
        if (updShip.getShipType() != null) {
            ship.setShipType(updShip.getShipType());
        }
        if (updShip.getProdDate() != null) {
            validator.checkProdDate(updShip.getProdDate());
            ship.setProdDate(updShip.getProdDate());
        }
        if (updShip.isUsed() != null) {
            ship.setUsed(updShip.isUsed());
        }
        if (updShip.getSpeed() != null) {
            validator.checkSpeed(updShip.getSpeed());
            ship.setSpeed(updShip.getSpeed());
        }
        if (updShip.getCrewSize() != null) {
            validator.checkCrewSize(updShip.getCrewSize());
            ship.setCrewSize(updShip.getCrewSize());
        }
        ship.setRating(validator.calcRating(ship.getSpeed(), ship.isUsed(), ship.getProdDate()));
        return shipService.saveAndFlushShip(ship);
    }

    //Delete Ship
    @DeleteMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteShip(@PathVariable String id) {
        Long longId = validator.getValidId(id);
        if (shipService.isExist(longId)) {
            shipService.deleteShip(longId);
        }
        else throw new NotFoundException("ship`s not found");
    }
}
