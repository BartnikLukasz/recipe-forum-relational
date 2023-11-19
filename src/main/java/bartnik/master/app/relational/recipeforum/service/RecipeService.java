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
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static bartnik.master.app.relational.recipeforum.model.QRecipe.recipe;

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

    public Page<Recipe> findRecipes(RecipesFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), Sort.by(filter.getSortBy()));
        return recipeRepository.findAll(buildPredicate(filter), pageable);
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

    public Recipe rateRecipe(UUID id, boolean liked) {
        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());
        var recipe = recipeRepository.getReferenceById(id);

        if (!recipeRepositoryCrud.isReactedByUser(id, user.getId(), liked)) {
            var likedRecipes = user.getLikedRecipes();
            var dislikedRecipes = user.getDislikedRecipes();

            if (liked) {
                likedRecipes.add(recipe);
                dislikedRecipes.removeIf(dislikedRecipe -> {
                    if (dislikedRecipe.equals(recipe)) {
                        recipe.setNumberOfDislikes(recipe.getNumberOfDislikes() - 1);
                        return true;
                    }
                    return false;
                });
                recipe.setNumberOfLikes(recipe.getNumberOfLikes() + 1);
            } else {
                dislikedRecipes.add(recipe);
                likedRecipes.removeIf(likedRecipe -> {
                    if (likedRecipe.equals(recipe)) {
                        recipe.setNumberOfLikes(recipe.getNumberOfLikes() - 1);
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

    private Predicate buildPredicate(RecipesFilterRequest filter) {
        var booleanBuilder = new BooleanBuilder();

        Optional.ofNullable(filter.getUserId()).ifPresent(userId -> booleanBuilder.and(recipe.user.id.eq(userId)));
        Optional.ofNullable(filter.getTitleContains()).ifPresent(titleContains -> booleanBuilder.and(recipe.title.contains(titleContains)));
        Optional.ofNullable(filter.getContentContains()).ifPresent(contentContains -> booleanBuilder.and(recipe.content.contains(contentContains)));
        Optional.ofNullable(filter.getIngredientsContains()).ifPresent(ingredientsContains -> booleanBuilder.and(recipe.ingredients.contains(ingredientsContains)));
        Optional.ofNullable(filter.getTagsContains()).ifPresent(tagsContains -> booleanBuilder.and(recipe.tags.contains(tagsContains)));
        if (!filter.getCategoryIds().isEmpty()) {
            booleanBuilder.and(recipe.category.id.in(filter.getCategoryIds()));
        }

        return booleanBuilder;
    }
}
