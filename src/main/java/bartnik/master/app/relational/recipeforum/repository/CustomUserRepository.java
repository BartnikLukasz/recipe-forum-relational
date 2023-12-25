package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.CustomUser;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, UUID>, QuerydslPredicateExecutor<Recipe> {
    Optional<CustomUser> findByUsername(String username);
    default CustomUser getByUsername(String username) {
        return findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    @Query(value = "WITH recommended_recipes AS (\n" +
            "    SELECT culr3.liked_recipe_id, culr3.custom_user_id, COUNT(culr.liked_recipe_id) as common_likes\n" +
            "    FROM custom_users_liked_recipes culr\n" +
            "    JOIN custom_users_liked_recipes culr2 ON culr.liked_recipe_id = culr2.liked_recipe_id \n" +
            "    JOIN custom_users_liked_recipes culr3 ON culr2.custom_user_id = culr3.custom_user_id\n" +
            "    WHERE culr.custom_user_id = UNHEX(REPLACE( :userId, \"-\",\"\"))\n" +
            "    AND culr2.custom_user_id != UNHEX(REPLACE( :userId , \"-\",\"\"))\n" +
            "    AND culr3.liked_recipe_id NOT IN (\n" +
            "        SELECT DISTINCT liked_recipe_id\n" +
            "        FROM custom_users_liked_recipes culr\n" +
            "        WHERE culr.custom_user_id = UNHEX(REPLACE( :userId , \"-\",\"\"))\n" +
            "    )\n" +
            "    GROUP BY culr3.liked_recipe_id, culr3.custom_user_id\n" +
            ")\n" +
            "SELECT recipe.id \n" +
            "FROM recipe\n" +
            "JOIN recommended_recipes ON recommended_recipes.liked_recipe_id = recipe.id\n" +
            "GROUP BY recipe.id\n" +
            "ORDER BY SUM(common_likes) DESC\n" +
            "LIMIT :size",
            nativeQuery = true)
    List<byte[]> getRecommendations(@Param("userId") String userId, @Param("size") Integer size);
}
