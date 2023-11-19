package bartnik.master.app.relational.recipeforum.mapper;

import bartnik.master.app.relational.recipeforum.dto.response.CategoryLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.CategoryResponse;
import bartnik.master.app.relational.recipeforum.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse map(Category category);

    @Named("categoryLite")
    CategoryLiteResponse mapLite(Category category);

    default List<CategoryLiteResponse> map(List<Category> allCategories) {
        return allCategories.stream()
                .map(this::mapLite)
                .collect(Collectors.toList());
    }
}
