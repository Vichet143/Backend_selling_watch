package com.example.practice.controller;

import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.dto.WatchImageRequest;
import com.example.practice.entity.WatchImage;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.WatchImageRepository;
import com.example.practice.service.CloudinaryService;
import com.example.practice.service.WatchImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/watchimage")
@RequiredArgsConstructor
public class WatchImageController {
    private final WatchImageService watchImageService;


    @PreAuthorize("hasAuthority('watchimage:write')")
    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute WatchImageRequest watchImageRequest){

        try{
            WatchImage watchImage = watchImageService.createWatchImage(watchImageRequest);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Create watch image success",
                    watchImage
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"false", e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('watchimage:write')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateWatchImage(@PathVariable Long id, @ModelAttribute WatchImageRequest watchImageRequest){
       try{
           WatchImage watchImage = watchImageService.updateImageById(id, watchImageRequest);
           ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                   true,
                   "Update watch image success",
                   watchImage
           );

           return ResponseEntity.ok().body(responseMessageDTO);
       } catch (Exception e) {
           throw new ApiException(HttpStatus.BAD_REQUEST,"false", e.getMessage());
       }
    }

    @PreAuthorize("hasAuthority('watchimage:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWatchImage(@PathVariable Long id){
        watchImageService.deleteById(id);
        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Delete watch image success",
                "Delete watch image with id " + id
        );
        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @PreAuthorize("hasAuthority('watchimage:read')")
    @GetMapping
    public ResponseEntity<?> getAllWatchImage(@RequestParam Map<String, String> param){
        Page<WatchImage> allWatchImage = watchImageService.getAllWatchImage(param);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retrieve data watch image",
                allWatchImage
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }
}
