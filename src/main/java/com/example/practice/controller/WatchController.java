package com.example.practice.controller;

import com.example.practice.dto.RequestWatchDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Watch;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.WatchMapper;
import com.example.practice.service.WatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/watch")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;
    private final WatchMapper watchMapper;

    @PostMapping
    public ResponseEntity<?> createWatch(@RequestBody RequestWatchDTO requestWatchDTO){
        try{
            Watch watch1 = watchService.createWatch(requestWatchDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Create Watch Success",
                    watch1
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateWatch(@PathVariable Long id, @RequestBody RequestWatchDTO requestWatchDTO){
        try{
            Watch watch = watchService.updateWatchById(id, requestWatchDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Update success",
                    watch
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWatch(@PathVariable Long id){
        watchService.deleteWatchById(id);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Delete Success",
                "Delete watch at id = " + id
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllWatch(@RequestParam Map<String , String> param){

        Page<Watch> allWatch = watchService.getAllWatch(param);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retriveve Data watch",
                allWatch
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }
}
