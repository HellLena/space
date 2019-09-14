package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ShipSpecification {

    public static Specification<Ship> paramContains(final Object value, String paramName) {
        return (Specification<Ship>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(paramName), "%" + value + "%");
    }

    public static Specification<Ship> paramEquals(final Object value, String paramName) {
        return (Specification<Ship>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(paramName), value);
    }

    public static Specification<Ship> paramGE(final Number value, String paramName) {
        return (Specification<Ship>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get(paramName), value);
    }

    public static Specification<Ship> paramLE(final Number value, String paramName) {
        return (Specification<Ship>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get(paramName), value);
    }

    public static Specification<Ship> dateGE(final Date value, String paramName) {
        return (Specification<Ship>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(paramName), value);
    }

    public static Specification<Ship> dateLE(final Date value, String paramName) {
        return (Specification<Ship>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(paramName), value);
    }

}
