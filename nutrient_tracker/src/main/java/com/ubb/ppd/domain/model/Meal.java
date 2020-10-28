package com.ubb.ppd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Table(name = "meals", catalog = "nutrientTracker")
public class Meal implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "meal_date")
    private LocalDateTime mealDate;

    @ManyToMany
    @JoinTable(
            name = "meals_foods",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods;

    @Override
    public Long getId() {
        return this.id;
    }
}
