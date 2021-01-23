package com.space.utils;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;


public class ShipSpecifications {

    public Specification<Ship> selectByName(final String name) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (name == null) return null;
                return builder.like(root.get("name"), "%" + name + "%");
            }
        };
    }

    public Specification<Ship> selectByPlanet(final String planet) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (planet == null) return null;
                return builder.like(root.get("planet"), "%" + planet + "%");
            }
        };
    }

    public Specification<Ship> selectByType(final ShipType shipType) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (shipType == null) return null;
                return builder.equal(root.get("shipType"), shipType);
            }
        };
    }

    public Specification<Ship> selectByDate(final Long after, final Long before) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (after == null && before == null) return null;
                else if (after == null)
                    return builder.lessThanOrEqualTo(root.get("prodDate"), new Date(before));
                else if (before == null)
                    return builder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after));
                else return builder.between(root.get("prodDate"), new Date(after), new Date(before));
            }
        };
    }

    public Specification<Ship> selectByUsed(final Boolean isUsed) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (isUsed == null) return null;
                return builder.equal(root.get("isUsed"), isUsed);
            }
        };
    }

    public Specification<Ship> selectBySpeed(final Double minSpeed, final Double maxSpeed) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (minSpeed == null && maxSpeed == null) return null;
                else if (minSpeed == null)
                    return builder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
                else if (maxSpeed == null)
                    return builder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
                else return builder.between(root.get("speed"), minSpeed, maxSpeed);
            }
        };
    }

    public Specification<Ship> selectByCrewSize(final Integer minCrewSize, final Integer maxCrewSize) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (minCrewSize == null && maxCrewSize == null) return null;
                else if (minCrewSize == null)
                    return builder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
                else if (maxCrewSize == null)
                    return builder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
                else return builder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
            }
        };
    }

    public Specification<Ship> selectByRating(final Double minRating, final Double maxRating) {
        return new Specification<Ship>() {
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (minRating == null && maxRating == null) return null;
                else if (minRating == null)
                    return builder.lessThanOrEqualTo(root.get("rating"), maxRating);
                else if (maxRating == null)
                    return builder.greaterThanOrEqualTo(root.get("rating"), minRating);
                else return builder.between(root.get("rating"), minRating, maxRating);
            }
        };
    }
}
