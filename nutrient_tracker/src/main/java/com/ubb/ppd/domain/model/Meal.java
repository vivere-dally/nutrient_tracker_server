package com.ubb.ppd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    @Column(name = "u_lmod_id", columnDefinition = "integer DEFAULT 0", nullable = false)
    private long version;

    @Override
    public Long getId() {
        return this.id;
    }
}
