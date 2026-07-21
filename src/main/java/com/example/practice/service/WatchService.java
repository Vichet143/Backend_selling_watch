package com.example.practice.service;

import com.example.practice.dto.RequestWatchDTO;
import com.example.practice.entity.Watch;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface WatchService {
    Watch createWatch(RequestWatchDTO requestWatchDTO);
    Watch findById(Long id);
    Watch findByName(String name);
    Watch updateWatchById(Long id, RequestWatchDTO requestWatchDTO);
    void deleteWatchById(Long id);
    Page<Watch> getAllWatch(Map<String , String > param);
}
