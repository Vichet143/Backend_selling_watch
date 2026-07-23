package com.example.practice.controller;


import com.example.practice.dto.InventoryRequestDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Inventory;
import com.example.practice.exception.ApiException;
import com.example.practice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PreAuthorize("hasAuthority('inventory:write')")
    @PostMapping
    public ResponseEntity<?> create (@Valid @RequestBody InventoryRequestDTO inventoryRequestDTO){
        try{
            Inventory inventory = inventoryService.create(inventoryRequestDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Create inventory success",
                    inventory
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('inventory:write')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody InventoryRequestDTO inventoryRequestDTO){

        try {
            Inventory inventory = inventoryService.updateById(id, inventoryRequestDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Update inventory success",
                    inventory
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('inventory:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        inventoryService.deleteById(id);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Delete success",
                "Delete inventory at id " + id
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @PreAuthorize("hasAuthority('inventory:read')")
    @GetMapping
    public ResponseEntity<?> getAllInventory(@RequestParam Map<String, String> param){
        Page<Inventory> allInventory = inventoryService.getAllInventory(param);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retrieve data success",
                allInventory
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }
}
