package com.perepalacin.product_service.helpers;

import com.perepalacin.product_service.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                brand == null ? null : criteriaBuilder.equal(root.get("brand"), brand);
    }

    public static Specification<Product> hasMechanism(String mechanism) {
        return (root, query, criteriaBuilder) ->
                mechanism == null ? null : criteriaBuilder.equal(root.get("mechanism"), mechanism);
    }

    public static Specification<Product> hasDiameter(Integer diameter) {
        return (root, query, criteriaBuilder) ->
                diameter == null ? null : criteriaBuilder.equal(root.get("diameter"), diameter);
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> containsText(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        };
    }
}
