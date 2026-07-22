package com.example.practice.repository;

import com.example.practice.entity.WatchImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchImageRepository extends JpaRepository<WatchImage, Long>, JpaSpecificationExecutor<WatchImage> {
}
