package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@Transactional
public class CustomUserRepositoryCrud {

    @Autowired
    MongoTemplate mongoTemplate;

    public Optional<CustomUser> findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.find(query, CustomUser.class).stream().findFirst();
    }

    public Set<Recipe> getRecommendations(Integer size, UUID userId) {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//
//        var culr = new QCustomUserLikedRecipes("culr");
//        var culr2 = new QCustomUserLikedRecipes("culr2");
//        var culr3 = new QCustomUserLikedRecipes("culr3");
//
//        Set<UserRatedRecipe> recommendedRecipes = queryFactory.select(culr3.recipeId, culr3.userId)
//                .from(culr, culr2, culr3)
//                .where(culr.userId.eq(userId)
//                        .and(culr.recipeId.eq(culr2.recipeId))
//                        .and(culr2.userId.ne(userId))
//                        .and(culr3.userId.eq(culr2.userId))
//                        .and(culr3.recipeId.notIn(
//                                JPAExpressions.select(customUserLikedRecipes.recipeId)
//                                        .from(customUserLikedRecipes)
//                                        .where(customUserLikedRecipes.userId.eq(userId))
//                                        .distinct()
//                        ))
//                )
//                .fetch().stream()
//                .map(tuple -> {
//                    UserRatedRecipe userRatedRecipe = new UserRatedRecipe();
//                    userRatedRecipe.setRecipeId(tuple.get(culr3.recipeId));
//                    userRatedRecipe.setUserId(tuple.get(culr3.userId));
//                    return userRatedRecipe;
//                })
//                .collect(Collectors.toSet());
//
//        return new HashSet<>(queryFactory.select(recipe)
//                .from(recipe, customUserLikedRecipes)
//                .where(customUserLikedRecipes.recipeId.eq(recipe.id)
//                        .and(customUserLikedRecipes.userId.in(recommendedRecipes.stream()
//                                .map(UserRatedRecipe::getUserId)
//                                .toList()))
//                        .and(customUserLikedRecipes.recipeId.in(recommendedRecipes.stream()
//                                .map(UserRatedRecipe::getRecipeId)
//                                .toList())))
//                .groupBy(recipe.id)
//                .orderBy(recipe.count().desc())
//                .limit(size)
//                .fetch());

        return null;
    }



    @Setter
    @Getter
    private class UserRatedRecipe {
        private UUID recipeId;
        private UUID userId;
    }
}
