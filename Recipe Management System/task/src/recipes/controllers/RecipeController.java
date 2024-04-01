package recipes.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.models.Recipe;
import recipes.services.AppUserDetails;
import recipes.services.RecipeService;
import recipes.dtos.SavedRecipeDto;
import recipes.dtos.GetRecipeDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final ModelMapper modelMapper;
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(ModelMapper modelMapper, RecipeService recipeService) {
        this.modelMapper = modelMapper;
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
    public ResponseEntity<?> deleteById(
            @PathVariable long id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return recipeService.findById(id).map(existingRecipe -> {
                    if (!recipeService.isChefOfRecipe(id, userDetails)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    recipeService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(
            @PathVariable long id,
            @RequestBody @Valid Recipe updatedRecipe,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return recipeService.findById(id)
                .map(existingRecipe -> {
                    if (!recipeService.isChefOfRecipe(id, userDetails)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    recipeService.updateRecipe(id, updatedRecipe, userDetails);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetRecipeDto>> search(
            @RequestParam(required = false) @NotBlank String category,
            @RequestParam(required = false) @NotBlank String name) {

        List<GetRecipeDto> recipes;

        if (category != null ^ name != null) {
            recipes = category != null ?
                    recipeService.findByCategory(category) :
                    recipeService.findByName(name);
            return ResponseEntity.ok(recipes);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
