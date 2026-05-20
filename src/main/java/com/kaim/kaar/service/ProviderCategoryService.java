package com.kaim.kaar.service;

import com.kaim.kaar.entity.ProviderCategory;
import com.kaim.kaar.entity.ServiceCategory;
import com.kaim.kaar.entity.User;
import com.kaim.kaar.repository.ProviderCategoryRepository;
import com.kaim.kaar.repository.ServiceCategoryRepository;
import com.kaim.kaar.repository.UserRepositoy;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProviderCategoryService {
    @Autowired
    private ProviderCategoryRepository providerCategoryRepository;
    @Autowired
    private UserRepositoy userRepositoy;
    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;


    public void addService(ProviderCategory newProviderCategory){

        if (newProviderCategory == null) {
            throw new RuntimeException("Request body is empty");
        }

        if (newProviderCategory.getProvider() == null) {
            throw new RuntimeException("Provider is required");
        }

        if (newProviderCategory.getServiceCategory() == null) {
            throw new RuntimeException("Service Category is required");
        }
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User provider = userRepositoy.findByEmail(email);
        ServiceCategory service = serviceCategoryRepository.findById(newProviderCategory.getServiceCategory().getId()).orElseThrow(() -> new RuntimeException("NO SERVICE EXITS"));

        ProviderCategory providerCategory = new ProviderCategory();
        providerCategory.setDescription(newProviderCategory.getDescription());
        providerCategory.setPrice(newProviderCategory.getPrice());
        providerCategory.setLocation(newProviderCategory.getLocation());
        providerCategory.setProvider(provider);
        providerCategory.setServiceCategory(service);

       providerCategoryRepository.save(providerCategory);

    }
    public List<ProviderCategory> getProvidersByCategory(Long categoryId){

        return providerCategoryRepository
                .findByServiceCategory_Id(categoryId);
    }

    public List<ProviderCategory> getAllProviders() {
        return providerCategoryRepository.findAll();
    }
}
