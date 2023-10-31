package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.CreateRecipeRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateRecipeRequest;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeResponse;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import bartnik.master.app.relational.recipeforum.repository.CategoryRepository;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.RecipeRepository;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final CustomUserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    public Recipe createRecipe(CreateRecipeRequest request) {
        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());
        var category = categoryRepository.getReferenceById(request.getCategory());

        var recipe = Recipe.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .ingredients(request.getIngredients())
                .tags(request.getTags())
                .user(user)
                .category(category)
                .build();

        return recipeRepository.save(recipe);
    }

    public Recipe getRecipeById(UUID id) {
        return recipeRepository.getReferenceById(id);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe updateRecipe(UUID id, UpdateRecipeRequest request) {
        var recipe = recipeRepository.getReferenceById(id);
        var category = recipe.getCategory();

        if (!recipe.getCategory().getId().equals(request.getCategory())) {
            category = categoryRepository.getReferenceById(request.getCategory());
            recipe.setCategory(category);
        }
        recipe.apply(request);

        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(UUID id) {
        var currentUser = UserUtil.getCurrentUser();
        var recipe = recipeRepository.getReferenceById(id);

        if (!currentUser.getUsername().equals(recipe.getUser().getUsername()) || UserUtil.isCurrentUserAdmin()) {
            throw new AccessDeniedException("User is not admin or the owner of this recipe.");
        }

        recipeRepository.deleteById(id);
    }
}
