package recipes.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.dtos.SavedRecipeDto;
import recipes.dtos.GetRecipeDto;
import recipes.models.AppUser;
import recipes.models.Recipe;
import recipes.repositories.AppUserRepository;
import recipes.repositories.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;
    private final AppUserRepository userRepository;

    @Autowired
    public RecipeService(ModelMapper modelMapper, RecipeRepository recipeRepository, AppUserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public SavedRecipeDto saveRecipe(Recipe recipe, UserDetails userDetails) {
        return userRepository.findById(userDetails.getUsername())
                .map(user -> {
                    recipe.setChef(user);
                    Recipe savedRecipe = recipeRepository.save(recipe);
                    return modelMapper.map(savedRecipe, SavedRecipeDto.class);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
    }

    public Optional<GetRecipeDto> findById(long id) {
        return recipeRepository.findById(id).map(recipe -> modelMapper.map(recipe, GetRecipeDto.class));
    }

    public void deleteById(long id) {
        recipeRepository.deleteById(id);
    }

    public void updateRecipe(long recipeId, Recipe recipe, UserDetails userDetails) {
        userRepository.findById(userDetails.getUsername())
                .ifPresentOrElse(
                        user -> {
                            recipe.setChef(user);
                            recipe.setId(recipeId);
                            recipeRepository.save(recipe);
                        },
                        () -> {
                            throw new UsernameNotFoundException("User not found: " + userDetails.getUsername());
                        });
    }

    public List<GetRecipeDto> findByCategory(String category) {
        List<Recipe> recipes = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        return mapToGetRecipeDtoList(recipes);
    }

    private List<GetRecipeDto> mapToGetRecipeDtoList(List<Recipe> recipes) {
        return recipes.stream()
                .map(recipe -> modelMapper.map(recipe, GetRecipeDto.class))
                .toList();
    }

    public List<GetRecipeDto> findByName(String name) {
        List<Recipe> recipes = recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name);
        return mapToGetRecipeDtoList(recipes);
    }

    public boolean isChefOfRecipe(long recipeId, AppUserDetails userDetails) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + recipeId));
        return userRepository.findById(userDetails.getUsername())
                .map(user -> user.equals(recipe.getChef()))
                .orElse(false);
    }
}
