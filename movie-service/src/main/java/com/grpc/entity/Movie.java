package com.grpc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Movie {

    @Id
    private int id;
    private String title;
    @Column (name = "release_year")
    private int year;
    private double rating;
    private String genre;
}
