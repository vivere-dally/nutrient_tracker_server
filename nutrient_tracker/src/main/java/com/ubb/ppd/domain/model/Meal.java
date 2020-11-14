package com.ubb.ppd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "foods")
    private String foods;

    @Column(name = "eaten")
    private boolean eaten;

    @Column(name = "price")
    private float price;

    @Override
    public Long getId() {
        return this.id;
    }
}
