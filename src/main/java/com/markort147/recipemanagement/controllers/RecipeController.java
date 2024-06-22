package com.markort147.recipemanagement.controllers;

import com.markort147.recipemanagement.dtos.GetRecipeDto;
import com.markort147.recipemanagement.dtos.SavedRecipeDto;
import com.markort147.recipemanagement.models.Recipe;
import com.markort147.recipemanagement.services.AppUserDetails;
import com.markort147.recipemanagement.services.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetRecipeDto> getById(@PathVariable long id) {
        Optional<GetRecipeDto> recipe = recipeService.findById(id);
        return recipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public SavedRecipeDto postNew(@RequestBody @Valid Recipe recipe, @AuthenticationPrincipal AppUserDetails userDetails) {
        return recipeService.saveRecipe(recipe, userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable long id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        if (recipeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!recipeService.isChefOfRecipe(id, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(
            @PathVariable long id,
            @RequestBody @Valid Recipe updatedRecipe,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        if (recipeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!recipeService.isChefOfRecipe(id, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        recipeService.updateRecipe(id, updatedRecipe, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetRecipeDto>> search(
            @RequestParam(required = false) @NotBlank String category,
            @RequestParam(required = false) @NotBlank String name) {
        if (category != null ^ name != null) {
            List<GetRecipeDto> recipes = category != null ? recipeService.findByCategory(category) : recipeService.findByName(name);
            return ResponseEntity.ok(recipes);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
