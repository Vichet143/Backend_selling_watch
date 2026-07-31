package com.example.practice.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PageSpec<T> implements Specification<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    // search by name
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

    // search by id
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

    // search by id in other table
    public PageSpec<T> equalJoin(String relation, String field, Object value) {

        if (value != null) {
            specifications.add((root, query, cb) ->
                    cb.equal(root.get(relation).get(field), value)
            );
        }

        return this;
    }

    //search for get data between date
    public PageSpec<T> betweenLocalDate(
            String field,
            LocalDate start,
            LocalDate end
    ) {

        if (start != null) {
            specifications.add((root, query, cb) ->
                    cb.greaterThanOrEqualTo(
                            root.get(field),
                            start.atStartOfDay()
                    ));
        }

        if (end != null) {
            specifications.add((root, query, cb) ->
                    cb.lessThan(
                            root.get(field),
                            end.plusDays(1).atStartOfDay()
                    ));
        }

        return this;
    }


    //search only one date
    public PageSpec<T> startDate(String field, LocalDate date) {

        if (date != null) {

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            specifications.add((root, query, cb) ->
                    cb.and(
                            cb.greaterThanOrEqualTo(root.get(field), start),
                            cb.lessThan(root.get(field), end)
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
