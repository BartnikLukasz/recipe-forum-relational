package bartnik.master.app.relational.recipeforum.repository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
@Transactional
public class RecipeRepositoryCrud {

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
