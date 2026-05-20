package com.kaim.kaar.repository;

import com.kaim.kaar.entity.ProviderCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderCategoryRepository extends JpaRepository<ProviderCategory,Long> {
    List<ProviderCategory> findByServiceCategory_Id(Long id);
}
