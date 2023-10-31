package bartnik.master.app.relational.recipeforum.mapper;

import bartnik.master.app.relational.recipeforum.dto.response.CategoryLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.CategoryResponse;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeResponse;
import bartnik.master.app.relational.recipeforum.model.Category;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ap.internal.processor.MapperServiceProcessor;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse map(Category category);

    @Named("categoryLite")
    CategoryLiteResponse mapLite(Category category);
}
