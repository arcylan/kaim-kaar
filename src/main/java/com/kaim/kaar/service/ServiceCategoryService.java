package com.kaim.kaar.service;

import com.kaim.kaar.entity.ServiceCategory;
import com.kaim.kaar.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    public void addCategory(String name){
        if(name==null){
            throw new RuntimeException("Service cannot be null");
        }
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setName(name);
        serviceCategoryRepository.save(serviceCategory);
    }

    public List<ServiceCategory> getAllCategory(){
        List<ServiceCategory> all = serviceCategoryRepository.findAll();
        if(all==null){
            throw new RuntimeException("No Categories Found");
        }
        return all;
    }
}
