package com.markort147.recipemanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @Size(min = 1)
    @NotNull
    @ElementCollection
    private List<String> ingredients;

    @Size(min = 1)
    @NotNull
    @ElementCollection
    private List<String> directions;

    @UpdateTimestamp
    LocalDateTime date;

    @ManyToOne
    private AppUser chef;
}
