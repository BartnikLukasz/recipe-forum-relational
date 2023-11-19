package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static bartnik.master.app.relational.recipeforum.model.QCustomUserLikedRecipes.customUserLikedRecipes;
import static bartnik.master.app.relational.recipeforum.model.QRecipe.*;

@Component
@Transactional
public class CustomUserRepositoryCrud {

    @PersistenceContext(name = "recipe-forum")
    EntityManager em;

    public Set<Recipe> getRecommendations(Integer size, UUID userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        var culr = new QCustomUserLikedRecipes("culr");
        var culr2 = new QCustomUserLikedRecipes("culr2");
        var culr3 = new QCustomUserLikedRecipes("culr3");

        Set<UserRatedRecipe> recommendedRecipes = queryFactory.select(culr3.recipeId, culr3.userId)
                .from(culr, culr2, culr3)
                .where(culr.userId.eq(userId)
                        .and(culr.recipeId.eq(culr2.recipeId))
                        .and(culr2.userId.ne(userId))
                        .and(culr3.userId.eq(culr2.userId))
                        .and(culr3.recipeId.notIn(
                                JPAExpressions.select(customUserLikedRecipes.recipeId)
                                        .from(customUserLikedRecipes)
                                        .where(customUserLikedRecipes.userId.eq(userId))
                                        .distinct()
                        ))
                )
                .fetch().stream()
                .map(tuple -> {
                    UserRatedRecipe userRatedRecipe = new UserRatedRecipe();
                    userRatedRecipe.setRecipeId(tuple.get(culr3.recipeId));
                    userRatedRecipe.setUserId(tuple.get(culr3.userId));
                    return userRatedRecipe;
                })
                .collect(Collectors.toSet());

        return new HashSet<>(queryFactory.select(recipe)
                .from(recipe, customUserLikedRecipes)
                .where(customUserLikedRecipes.recipeId.eq(recipe.id)
                        .and(customUserLikedRecipes.userId.in(recommendedRecipes.stream()
                                .map(UserRatedRecipe::getUserId)
                                .toList()))
                        .and(customUserLikedRecipes.recipeId.in(recommendedRecipes.stream()
                                .map(UserRatedRecipe::getRecipeId)
                                .toList())))
                .groupBy(recipe.id)
                .orderBy(recipe.count().desc())
                .limit(size)
                .fetch());
    }

    @Setter
    @Getter
    private class UserRatedRecipe {
        private UUID recipeId;
        private UUID userId;
    }
}
