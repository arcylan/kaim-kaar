package com.kaim.kaar.controller;

import com.kaim.kaar.entity.ProviderCategory;
import com.kaim.kaar.service.ProviderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider")
public class ProviderCategoryController {
    @Autowired
    private ProviderCategoryService providerCategoryService;

    @PostMapping("/service")
    public ResponseEntity<?> addService(@RequestBody ProviderCategory providerCategory){
        providerCategoryService.addService(providerCategory);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Details Added Successfully");

    }
    @GetMapping("/service/{categoryId}")
    public ResponseEntity<List<ProviderCategory>> getProviders(
            @PathVariable Long categoryId){

        return ResponseEntity.ok(
                providerCategoryService
                        .getProvidersByCategory(categoryId)
        );
    }
    @GetMapping("/services")
    public ResponseEntity<List<ProviderCategory>> getAllProviders(){
        return ResponseEntity.ok(
                providerCategoryService.getAllProviders()
        );
    }
}
