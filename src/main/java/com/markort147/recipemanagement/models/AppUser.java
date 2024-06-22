package com.markort147.recipemanagement.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class AppUser {
    @Id
    private String username;
    private String password;
    private String authority;

    @OneToMany
    private List<Recipe> recipes;
}
