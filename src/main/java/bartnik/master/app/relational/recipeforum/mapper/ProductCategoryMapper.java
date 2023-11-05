package bartnik.master.app.relational.recipeforum.mapper;

import bartnik.master.app.relational.recipeforum.dto.response.CategoryLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.CategoryResponse;
import bartnik.master.app.relational.recipeforum.dto.response.ProductCategoryLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.ProductCategoryResponse;
import bartnik.master.app.relational.recipeforum.model.Category;
import bartnik.master.app.relational.recipeforum.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryResponse map(ProductCategory category);

    @Named("productCategoryLite")
    ProductCategoryLiteResponse mapLite(ProductCategory category);
}
