package com.example.practice.service.impl;

import com.example.practice.dto.InventoryRequestDTO;
import com.example.practice.entity.Inventory;
import com.example.practice.entity.Watch;
import com.example.practice.entity.WatchImage;
import com.example.practice.entity.WatchStatus;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.InventoryMapper;
import com.example.practice.repository.InventoryRepository;
import com.example.practice.repository.WatchRepository;
import com.example.practice.service.InventoryService;
import com.example.practice.service.WatchService;
import com.example.practice.service.util.PageUtil;
import com.example.practice.spec.PageFilter;
import com.example.practice.spec.PageSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final WatchService watchService;
    private final InventoryMapper inventoryMapper;
    private final WatchRepository watchRepository;

    @Override
    public Inventory create(InventoryRequestDTO inventoryRequestDTO) {
        Watch watch = watchService.findById(inventoryRequestDTO.getWatchId());
        Inventory inventory = inventoryMapper.toInventory(inventoryRequestDTO);
        inventory.setWatch(watch);

        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory findById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"false", "Inventory not found with id " + id));
    }

    @Override
    public Inventory updateById(Long id, InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = findById(id);

        if (inventoryRequestDTO.getWatchId() != null){
            Watch byId = watchService.findById(inventoryRequestDTO.getWatchId());
            inventory.setWatch(byId);
        }

        if (inventoryRequestDTO.getQuantity() != null){
            inventory.setQuantity(inventoryRequestDTO.getQuantity());
            Watch byId = watchService.findById(inventory.getWatch().getId());
            if (inventoryRequestDTO.getQuantity() == 0){
                byId.setStatus(WatchStatus.SOLD_OUT);
                watchRepository.save(byId);
            }else {
                byId.setStatus(WatchStatus.AVAILABLE);
            }
        }

        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteById(Long id) {
        Inventory byId = findById(id);

        inventoryRepository.deleteById(byId.getId());
    }

    @Override
    public Page<Inventory> getAllInventory(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }
        PageSpec<Inventory> pageSpec = new PageSpec<>();

        pageSpec.equal(
                "id",
                pageFilter.getId()
        );

        int pageLimit = PageUtil.DEFAULT_PAGE_LIMIT;
        if (param.containsKey( PageUtil.PAGE_LIMIT)){
            pageLimit = Integer.parseInt(param.get(PageUtil.PAGE_LIMIT));

        }

        int pageNumber = PageUtil.DEFAULT_PAGE_NUMBER;
        if (param.containsKey( PageUtil.PAGE_SIZE)){
            pageNumber = Integer.parseInt(param.get(PageUtil.PAGE_SIZE));

        }

        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);

        return inventoryRepository.findAll(pageSpec,pageable);
    }
}
