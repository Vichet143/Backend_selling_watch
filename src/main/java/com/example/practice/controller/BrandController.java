package com.example.practice.controller;

import com.example.practice.dto.BrandRequestDTO;
import com.example.practice.dto.PageDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Brand;
import com.example.practice.entity.Category;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.BrandMapper;
import com.example.practice.service.BrandService;
import com.example.practice.service.CloudinaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {
    private final CloudinaryService cloudinaryService;
    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @PreAuthorize("hasAuthority('brand:write')")
    @PostMapping
    public ResponseEntity<?> createBrand(@ModelAttribute BrandRequestDTO brandRequestDTO) {

        String imageUrl = cloudinaryService.uploadImage(brandRequestDTO.getLogo());

        Brand brand = new Brand();
        brand.setName(brandRequestDTO.getName());
        brand.setCountry(brandRequestDTO.getCountry());
        brand.setLogo(imageUrl);
        Brand brand1 = brandService.createBrand(brand);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Create brand success",
                brand1
        );

        return ResponseEntity.ok(responseMessageDTO);
    }

    @PreAuthorize("hasAuthority('brand:write')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBrandById( @PathVariable Long id,@ModelAttribute BrandRequestDTO brandRequestDTO
    ) {
        try{
            Brand updatedBrand = brandService.updateBrandById(id, brandMapper.toBrand(brandRequestDTO));

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Update brand success",
                    updatedBrand
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false",e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('brand:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrandById(@PathVariable Long id){
        brandService.deleteBrandById(id);
        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Category deleted successfully",
                "delete at id = " + id
        );
        return ResponseEntity.ok(responseMessageDTO);
    }

    @PreAuthorize("hasAnyAuthority('brand:read')")
    @GetMapping
    public ResponseEntity<?> getAllCategory(@RequestParam Map<String, String> param){
        Page<Brand> allCategory = brandService.getAllBrand(param);
        PageDTO pageDTO = new PageDTO(allCategory);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retrieve data success",
                pageDTO
        );
        return ResponseEntity.ok(responseMessageDTO);
    }
}
