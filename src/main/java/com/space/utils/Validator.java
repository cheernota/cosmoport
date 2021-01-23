package com.space.utils;


import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

public class Validator {

    //validate Id
    public Long getValidId(String strId) {
        long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("wrong id");
        }
        if (id > 0) return id;
        else throw new BadRequestException("wrong id");
    }

    //checker`s
    public void checkName(String name) {
        if (name == null || name.length() > 50 || StringUtils.isEmpty(name))
            throw new BadRequestException("incorrect Name");
    }

    public void checkPlanet(String planet) {
        if (planet == null || planet.length() > 50 || StringUtils.isEmpty(planet))
            throw new BadRequestException("incorrect Planet");
    }

    public void checkSpeed(Double speed) {
        if (speed < 0.01D || speed > 0.99D)
            throw new BadRequestException("wrong Speed`s value");
    }

    public void checkCrewSize(Integer crewSize) {
        if (crewSize < 1 || crewSize > 9999)
            throw new BadRequestException("wrong number of crewSize");
    }

    public void checkProdDate(Date prodDate) {
        if (prodDate.getTime() < 0)
            throw new BadRequestException("wrong prodDate`s value");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prodDate);
        if (calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019)
            throw new BadRequestException("wrong prodDate - must between 2800 and 3019");
    }

    //calculate rating
    public Double calcRating(Double shipSpeed, Boolean isUsed, Date prodDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prodDate);
        double k = isUsed ? 0.5 : 1;
        return Math.rint((80 * shipSpeed * k)/(3019 - calendar.get(Calendar.YEAR) + 1)*100.0)/100.0;
    }
}
