package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.*;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

        var userLikedRecipes = mongoTemplate.find(Query.query(Criteria.where("id").is(userId)), CustomUser.class)
                .get(0).getLikedRecipes().stream().map(Recipe::getId).toList();

        var topUsers = mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("id").in(userLikedRecipes)),
                Aggregation.group("user").count().as("RecipeCount"),
                Aggregation.sort(Sort.Direction.DESC, "RecipeCount"),
                Aggregation.limit(5)
        ), Recipe.class, CustomUser.class).getMappedResults().stream().map(CustomUser::getId).toList();

        var topCategories = mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("id").in(userLikedRecipes)),
                Aggregation.group("category").count().as("RecipeCount"),
                Aggregation.sort(Sort.Direction.DESC, "RecipeCount"),
                Aggregation.limit(3)
        ), Recipe.class, Category.class).getMappedResults().stream().map(Category::getId).toList();

        var similarUsers = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("likedRecipes").in(userLikedRecipes).and("id").ne(userId)),
                        Aggregation.unwind("likedRecipes"),
                        Aggregation.group("id").addToSet("likedRecipes").as("likedRecipes")
                ), CustomUser.class, CustomUser.class).getMappedResults().stream().map(CustomUser::getId).toList();

        var recommendedRecipeIds = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("id").in(similarUsers)),
                        Aggregation.unwind("likedRecipes"),
                        Aggregation.match(Criteria.where("likedRecipes").nin(userLikedRecipes)),
                        Aggregation.group("likedRecipes").count().as("count"),
                        Aggregation.project()
                                .and("count").as("count")
                                .and("likedRecipes.user").as("user")
                                .and("likedRecipes.category").as("category"),
                        Aggregation.project()
                                .and("count").as("count")
                                .and("user").as("user")
                                .and("category").as("category")
                                .and(ConditionalOperators.when(Criteria.where("user").in(topUsers)).then(2).otherwise(1)).as("userMultiplier"),
                        Aggregation.project()
                                .and("count").as("count")
                                .and("user").as("user")
                                .and("category").as("category")
                                .and("userMultiplier").as("userMultiplier")
                                .and(ConditionalOperators.when(Criteria.where("category").in(topCategories)).then(2).otherwise(1)).as("categoryMultiplier"),
                        Aggregation.project()
                                .and("count").multiply("userMultiplier").multiply("categoryMultiplier").as("adjustedCount"),
                        Aggregation.sort(Sort.Direction.DESC, "adjustedCount"),
                        Aggregation.limit(size)
                ), CustomUser.class, Recipe.class);

        return new HashSet<>(mongoTemplate.find(Query.query(Criteria.where("id").in(recommendedRecipeIds.getMappedResults().stream().map(Recipe::getId).collect(Collectors.toList()))), Recipe.class));

    }
}
