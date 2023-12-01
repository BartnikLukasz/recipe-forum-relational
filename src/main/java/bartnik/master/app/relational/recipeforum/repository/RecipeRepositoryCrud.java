package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.dto.request.RecipesFilterRequest;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@Component
@Transactional
public class RecipeRepositoryCrud {

    @Autowired
    MongoTemplate mongoTemplate;

    public Page<Recipe> findAll(RecipesFilterRequest filter, Pageable pageable) {
        Query query = new Query();

        Optional.ofNullable(filter.getUserId()).ifPresent(userId -> query.addCriteria(where("_id").is(userId)));
        Optional.ofNullable(filter.getTitleContains()).ifPresent(titleContains -> query.addCriteria(where("title").regex(Pattern.compile(Pattern.quote(titleContains), Pattern.CASE_INSENSITIVE))));
        Optional.ofNullable(filter.getContentContains()).ifPresent(contentContains -> query.addCriteria(where("title").regex(Pattern.compile(Pattern.quote(contentContains), Pattern.CASE_INSENSITIVE))));
        Optional.ofNullable(filter.getIngredientsContains()).ifPresent(ingredientsContains -> query.addCriteria(where("title").regex(Pattern.compile(Pattern.quote(ingredientsContains), Pattern.CASE_INSENSITIVE))));
        Optional.ofNullable(filter.getTagsContains()).ifPresent(tagsContains -> query.addCriteria(where("title").regex(Pattern.compile(Pattern.quote(tagsContains), Pattern.CASE_INSENSITIVE))));
        if (!filter.getCategoryIds().isEmpty()) {
            query.addCriteria(where("category").in(filter.getCategoryIds()));
        }
        query.with(pageable);
        List<Recipe> recipes = mongoTemplate.find(query, Recipe.class);
        return PageableExecutionUtils.getPage(recipes, pageable,
                () -> mongoTemplate.count((Query.of(query).limit(-1).skip(-1)), Recipe.class));
    }

    public boolean isReactedByUser(UUID recipeId, UUID userId, boolean liked) {

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if (liked) {
//            builder.and(customUser.likedRecipes.any().id.eq(recipeId));
//        }
//        else {
//            builder.and(customUser.dislikedRecipes.any().id.eq(recipeId));
//        }
//
//        return queryFactory.select(customUser.id)
//                .from(customUser)
//                .where(customUser.id.eq(userId).and(builder))
//                .fetchOne() != null;
        return false;
    }

}
