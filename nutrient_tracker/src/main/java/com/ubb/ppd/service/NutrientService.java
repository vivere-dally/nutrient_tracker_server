package com.ubb.ppd.service;

import com.ubb.ppd.domain.model.dto.NutrientDTO;

import java.util.List;

public interface NutrientService {
    NutrientDTO saveNutrient(NutrientDTO nutrientDTO);

    NutrientDTO updateNutrient(NutrientDTO nutrientDTO);

    NutrientDTO removeNutrient(NutrientDTO nutrientDTO);

    NutrientDTO getNutrientById(Long id);

    List<NutrientDTO> getNutrients();
}
