package com.kaim.kaar.controller;

import com.kaim.kaar.entity.ServiceCategory;
import com.kaim.kaar.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryService serviceCategoryService;

    @PostMapping
    public ResponseEntity<?> addService(@RequestBody String name){
        serviceCategoryService.addCategory(name);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Service Added");
    }

    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getAllService(){
       return new ResponseEntity<>(serviceCategoryService.getAllCategory(),HttpStatus.OK);
    }
}
