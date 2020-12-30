package com.ubb.ppd.domain.model.dto;

import com.ubb.ppd.domain.model.Entity;

import java.io.Serializable;

public interface DTO<E extends Entity<T>, T extends Serializable> extends Serializable {
    E toEntity();
}
