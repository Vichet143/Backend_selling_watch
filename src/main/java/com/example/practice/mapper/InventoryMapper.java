package com.example.practice.mapper;

import com.example.practice.dto.InventoryRequestDTO;
import com.example.practice.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "watch", source = "watchId", ignore = true)
    Inventory toInventory(InventoryRequestDTO inventoryRequestDTO);
}
