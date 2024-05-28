package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

        var similarUsersWithCommonLikes = mongoTemplate.aggregate(
                        Aggregation.newAggregation(
                                Aggregation.match(Criteria.where("likedRecipes").in(userLikedRecipes).and("id").ne(userId)),
                                Aggregation.unwind("likedRecipes"),
                                Aggregation.match(Criteria.where("likedRecipes").in(userLikedRecipes)),
                                Aggregation.group("id")
                                        .count().as("commonLikes")
                                        .addToSet("likedRecipes").as("commonLikedRecipes"),
                                Aggregation.lookup(
                                        "CustomUser", // assuming the collection name is "users"
                                        "_id",
                                        "_id",
                                        "allUserLikes"
                                ),
                                Aggregation.unwind("allUserLikes"),
                                Aggregation.group("_id")
                                        .first("commonLikes").as("commonLikes")
                                        .first("commonLikedRecipes").as("commonLikedRecipes")
                                        .addToSet("allUserLikes.likedRecipes").as("allLikedRecipes")
                        ), CustomUser.class, Map.class)
                .getMappedResults().stream()
                .collect(Collectors.toMap(
                        map -> (UUID) map.get("_id"),
                        map -> new AbstractMap.SimpleEntry<>(((Number) map.get("commonLikes")).intValue(), ((List<List<UUID>>) map.get("allLikedRecipes")).stream().flatMap(List::stream).toList())
                ));

        var recommendedRecipeIdsAndCountMap = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("id").in(similarUsersWithCommonLikes.keySet())),
                        Aggregation.unwind("likedRecipes"),
                        Aggregation.match(Criteria.where("likedRecipes").nin(userLikedRecipes)),
                        Aggregation.group("likedRecipes").count().as("count"),
                        Aggregation.sort(Sort.Direction.DESC, "count"),
                        Aggregation.limit(size*10)
                ), CustomUser.class, Map.class).getMappedResults().stream()
                .collect(Collectors.toMap(map -> (UUID) map.get("_id"), map -> ((Number) map.get("count")).intValue()));

        Map<UUID, Integer> returned = new HashMap<>();

        for (Map.Entry<UUID, Integer> entry : recommendedRecipeIdsAndCountMap.entrySet()) {
            var recipeId = entry.getKey();
            AtomicReference<Integer> count = new AtomicReference<>(entry.getValue());

            similarUsersWithCommonLikes.forEach((uuid, map) -> {
                if (map.getValue().contains(recipeId)) {
                    count.updateAndGet(v -> v + map.getKey());
                }
            });

            returned.put(recipeId, count.get());
        }

        List<UUID> sortedRecipes = returned.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(size)
                .toList();

        return new HashSet<>(mongoTemplate.find(Query.query(Criteria.where("id").in(sortedRecipes)), Recipe.class));
    }
}
