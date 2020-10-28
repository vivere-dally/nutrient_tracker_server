package com.ubb.ppd.repository;

import com.ubb.ppd.domain.model.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
}
