package com.ubb.ppd.service;

import com.ubb.ppd.domain.exception.NutrientNotFoundException;
import com.ubb.ppd.domain.model.Nutrient;
import com.ubb.ppd.domain.model.dto.NutrientDTO;
import com.ubb.ppd.repository.NutrientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutrientServiceImpl implements NutrientService {
    private final NutrientRepository nutrientRepository;

    public NutrientServiceImpl(NutrientRepository nutrientRepository) {
        this.nutrientRepository = nutrientRepository;
    }

    @Override
    public NutrientDTO saveNutrient(NutrientDTO nutrientDTO) {
        return new NutrientDTO(this.nutrientRepository.save(nutrientDTO.toEntity()));
    }

    @Override
    public NutrientDTO updateNutrient(NutrientDTO nutrientDTO) {
        if (this.nutrientRepository.findById(nutrientDTO.getId()).isEmpty()) {
            throw new NutrientNotFoundException("The nutrient with the given ID was not found!");
        }

        return new NutrientDTO(this.nutrientRepository.save(nutrientDTO.toEntity()));
    }

    @Override
    public NutrientDTO removeNutrient(NutrientDTO nutrientDTO) {
        if (this.nutrientRepository.findById(nutrientDTO.getId()).isEmpty()) {
            throw new NutrientNotFoundException("The nutrient with the given ID was not found!");
        }

        this.nutrientRepository.deleteById(nutrientDTO.getId());
        return nutrientDTO;
    }

    @Override
    public NutrientDTO getNutrientById(Long id) {
        Optional<Nutrient> nutrientOptional = this.nutrientRepository.findById(id);

        return nutrientOptional
                .map(NutrientDTO::new)
                .orElseThrow(() -> new NutrientNotFoundException("The nutrient with the given ID was not found!"));
    }

    @Override
    public List<NutrientDTO> getNutrients() {
        return this.nutrientRepository.findAll()
                .stream()
                .map(NutrientDTO::new)
                .collect(Collectors.toList());
    }
}
