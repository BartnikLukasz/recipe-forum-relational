package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.CreateRecipeRequest;
import bartnik.master.app.relational.recipeforum.dto.request.RecipesFilterRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateRecipeRequest;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import bartnik.master.app.relational.recipeforum.repository.CategoryRepository;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.RecipeRepository;
import bartnik.master.app.relational.recipeforum.repository.RecipeRepositoryCrud;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final CustomUserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeRepositoryCrud recipeRepositoryCrud;

    public Recipe createRecipe(CreateRecipeRequest request) {
        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());
        var category = categoryRepository.findById(request.getCategory()).orElseThrow();

        var recipe = Recipe.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .ingredients(request.getIngredients())
                .tags(request.getTags())
                .user(user)
                .category(category)
                .build();

        recipe = recipeRepository.save(recipe);
        category.getRecipes().add(recipe);
        user.getRecipes().add(recipe);
        categoryRepository.save(category);
        userRepository.save(user);
        return recipe;
    }

    public Recipe getRecipeById(UUID id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    public Page<Recipe> findRecipes(RecipesFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), Sort.by(Sort.Direction.valueOf(filter.getDirection()), filter.getSortBy()));
        return recipeRepositoryCrud.findAll(filter, pageable);
    }

    public Recipe updateRecipe(UUID id, UpdateRecipeRequest request) {
        var recipe = recipeRepository.findById(id).orElseThrow();
        var category = recipe.getCategory();

        if (!recipe.getCategory().getId().equals(request.getCategory())) {
            category.getRecipes().remove(recipe);
            var newCategory = categoryRepository.findById(request.getCategory()).orElseThrow();
            recipe.setCategory(category);

            recipe.apply(request);

            recipe = recipeRepository.save(recipe);
            newCategory.getRecipes().add(recipe);
            categoryRepository.saveAll(Set.of(category, newCategory));
            return recipe;
        }
        recipe.apply(request);

        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(UUID id) {
        var currentUser = UserUtil.getCurrentUser();
        var recipe = recipeRepository.findById(id).orElseThrow();
        var category = categoryRepository.findById(recipe.getCategory().getId()).orElseThrow();
        category.getRecipes().remove(recipe);

        if (!currentUser.getUsername().equals(recipe.getUser().getUsername()) || UserUtil.isCurrentUserAdmin()) {
            throw new AccessDeniedException("User is not admin or the owner of this recipe.");
        }

        categoryRepository.save(category);
        recipeRepository.deleteById(id);
    }

    public Recipe rateRecipe(UUID id, boolean liked) {
        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());
        var recipe = recipeRepository.findById(id).orElseThrow();

        if (!recipeRepositoryCrud.isReactedByUser(id, user.getId(), liked)) {
            var likedRecipes = user.getLikedRecipes();
            var dislikedRecipes = user.getDislikedRecipes();
            var likedByUsers = recipe.getLikedByUsers();
            var dislikedByUsers = recipe.getDislikedByUsers();

            if (liked) {
                likedRecipes.add(recipe);
                likedByUsers.add(user);
                dislikedRecipes.removeIf(dislikedRecipe -> {
                    if (dislikedRecipe.equals(recipe)) {
                        recipe.setNumberOfDislikes(recipe.getNumberOfDislikes() - 1);
                        dislikedByUsers.remove(user);
                        return true;
                    }
                    return false;
                });
                recipe.setNumberOfLikes(recipe.getNumberOfLikes() + 1);
            } else {
                dislikedRecipes.add(recipe);
                dislikedByUsers.add(user);
                likedRecipes.removeIf(likedRecipe -> {
                    if (likedRecipe.equals(recipe)) {
                        recipe.setNumberOfLikes(recipe.getNumberOfLikes() - 1);
                        likedByUsers.remove(user);
                        return true;
                    }
                    return false;
                });
                recipe.setNumberOfDislikes(recipe.getNumberOfDislikes() + 1);
            }
            userRepository.save(user);
        }
        return recipeRepository.save(recipe);
    }
}
