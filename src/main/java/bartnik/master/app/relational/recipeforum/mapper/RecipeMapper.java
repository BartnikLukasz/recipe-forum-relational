package bartnik.master.app.relational.recipeforum.mapper;

import bartnik.master.app.relational.recipeforum.dto.response.RecipeDetailsResponse;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeResponse;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, CommentMapper.class})
public interface RecipeMapper {

    CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    RecipeResponse map(Recipe recipe);

    RecipeLiteResponse mapLite(Recipe recipe);

    @Mapping(target = "comments", qualifiedByName = "commentList")
    @Mapping(target = "category", qualifiedByName = "categoryLite")
    RecipeDetailsResponse mapDetails(Recipe recipe);

    default List<RecipeDetailsResponse> mapDetails(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::mapDetails)
                .toList();
    }

    default List<RecipeLiteResponse> mapLite(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::mapLite)
                .toList();
    }
}
