package com.example.practice.service;

import com.example.practice.dto.InventoryRequestDTO;
import com.example.practice.entity.Inventory;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface InventoryService {

    Inventory create(InventoryRequestDTO inventoryRequestDTO);
    Inventory findById(Long id);
    Inventory updateById(Long id, InventoryRequestDTO inventoryRequestDTO);
    void deleteById(Long id);
    Page<Inventory> getAllInventory (Map<String, String> param);
}
