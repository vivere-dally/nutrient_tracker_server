package com.ubb.ppd.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Table(name = "foods", catalog = "nutrientTracker")
public class Food implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(name = "image")
//    private byte[] image;

    @ManyToMany
    @JoinTable(
            name = "foods_nutrients",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrient_id")
    )
    private List<Nutrient> nutrients;

    @Override
    public Long getId() {
        return this.id;
    }
}
