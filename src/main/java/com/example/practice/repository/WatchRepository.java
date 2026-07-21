package com.example.practice.repository;

import com.example.practice.entity.Brand;
import com.example.practice.entity.User;
import com.example.practice.entity.Watch;
import com.example.practice.spec.PageSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Long>, JpaSpecificationExecutor<Watch> {
    Optional<Watch> findWatchByName(String name);
}
