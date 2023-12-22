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

    @Query(value = "with recommended_recipes as (SELECT culr3.liked_recipe_id, culr3.custom_user_id \n" +
                "          FROM custom_users_liked_recipes culr, custom_users_liked_recipes culr2, \n" +
                "               custom_users_liked_recipes culr3 \n" +
                "          WHERE culr.custom_user_id = UNHEX(REPLACE( :userId , \"-\",\"\"))\n" +
                "          and culr.liked_recipe_id = culr2.liked_recipe_id \n" +
                "          and culr2.custom_user_id != UNHEX(REPLACE( :userId , \"-\",\"\"))\n" +
                "      and culr3.custom_user_id = culr2.custom_user_id \n" +
                "      and culr3.liked_recipe_id not in (select distinct liked_recipe_id \n" +
                "          FROM custom_users_liked_recipes culr \n" +
                "          WHERE culr.custom_user_id = UNHEX(REPLACE( :userId , \"-\",\"\"))) \n" +
                "   )\n" +
                "SELECT recipe.id \n" +
                "FROM recipe, custom_users_liked_recipes\n" +
                "WHERE custom_users_liked_recipes.liked_recipe_id = recipe.id \n" +
                "and custom_users_liked_recipes.liked_recipe_id in (select liked_recipe_id from recommended_recipes)\n" +
                "and custom_users_liked_recipes.custom_user_id in (select custom_user_id from recommended_recipes)\n" +
                "GROUP BY recipe.id \n" +
                "ORDER BY count(1) desc \n" +
                "LIMIT :size",
            nativeQuery = true)
    List<byte[]> getRecommendations(@Param("userId") String userId, @Param("size") Integer size);
}
