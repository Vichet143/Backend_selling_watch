package com.example.practice.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PageSpec<T> implements Specification<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    public PageSpec<T> likeIgnoreCase(
            String field,
            String value
    ) {

        if (value != null) {

            specifications.add((root, query, cb) ->
                    cb.like(
                            cb.upper(root.get(field)),
                            "%" + value.toUpperCase() + "%"
                    )
            );
        }

        return this;
    }

    public PageSpec<T> equal(
            String field,
            Object value
    ) {

        if (value != null) {

            specifications.add((root, query, cb) ->
                    cb.equal(
                            root.get(field),
                            value
                    )
            );
        }

        return this;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root,@NonNull CriteriaQuery<?> query,CriteriaBuilder cb
    ) {

        List<Predicate> predicates = specifications
                .stream()
                .map(specification ->
                        specification.toPredicate(root, query, cb)
                )
                .toList();

        return cb.and(
                predicates.toArray(Predicate[]::new)
        );
    }
}
