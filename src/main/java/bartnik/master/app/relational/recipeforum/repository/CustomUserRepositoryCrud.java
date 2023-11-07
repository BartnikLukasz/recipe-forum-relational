package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.QCustomUser;
import bartnik.master.app.relational.recipeforum.model.QRecipe;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.query.criteria.JpaSubQuery;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static bartnik.master.app.relational.recipeforum.model.QCustomUser.*;
import static bartnik.master.app.relational.recipeforum.model.QRecipe.*;

@Component
@Transactional
public class CustomUserRepositoryCrud {

    @PersistenceContext(name = "recipe-forum")
    EntityManager em;

    public Set<Recipe> getRecommendations(Integer size, UUID userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        var culr1 = JPAExpressions.select(recipe.id, customUser.id)
                .from(recipe)
                .innerJoin(customUser).on(recipe.id.in(customUser.likedRecipes.any().id))
                .stream()
                .map(culr -> new UserLikedRecipe(culr.get(recipe.id), culr.get(customUser.id)))
                .toList();

        var culr2 = JPAExpressions.select(recipe.id, customUser.id)
                .from(recipe)
                .innerJoin(customUser).on(recipe.id.in(customUser.likedRecipes.any().id))
                .stream()
                .map(culr -> new UserLikedRecipe(culr.get(recipe.id), culr.get(customUser.id)))
                .toList();

        var culr3 = JPAExpressions.select(recipe.id, customUser.id)
                .from(recipe)
                .innerJoin(customUser).on(recipe.id.in(customUser.likedRecipes.any().id))
                .stream()
                .map(culr -> new UserLikedRecipe(culr.get(recipe.id), culr.get(customUser.id)))
                .toList();

        queryFactory.select(culr3.)
    }

    @AllArgsConstructor
    private class UserLikedRecipe{

        public UUID recipeId;
        public UUID userId;
    }

}
