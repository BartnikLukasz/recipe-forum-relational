package bartnik.master.app.relational.recipeforum.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static bartnik.master.app.relational.recipeforum.model.QCustomUser.customUser;

@Component
@Transactional
public class RecipeRepositoryCrud {

    @PersistenceContext(name = "recipe-forum")
    EntityManager em;

    public boolean isReactedByUser(UUID recipeId, UUID userId, boolean liked) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        BooleanBuilder builder = new BooleanBuilder();

        if (liked) {
            builder.and(customUser.likedRecipes.any().id.eq(recipeId));
        }
        else {
            builder.and(customUser.dislikedRecipes.any().id.eq(recipeId));
        }

        return queryFactory.select(customUser.id)
                .from(customUser)
                .where(customUser.id.eq(userId).and(builder))
                .fetchOne() != null;
    }

}
