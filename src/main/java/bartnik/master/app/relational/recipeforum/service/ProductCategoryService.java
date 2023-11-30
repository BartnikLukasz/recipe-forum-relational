package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.CreateProductCategoryRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateProductCategoryRequest;
import bartnik.master.app.relational.recipeforum.model.ProductCategory;
import bartnik.master.app.relational.recipeforum.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategory createProductCategory(CreateProductCategoryRequest request) {
        var productCategory = ProductCategory.builder()
                .name(request.getName())
                .build();

        return productCategoryRepository.save(productCategory);
    }

    public ProductCategory updateProductCategory(UUID id, UpdateProductCategoryRequest request) {
        var productCategory = productCategoryRepository.findById(id).orElseThrow();
        productCategory.setName(request.getName());
        return productCategoryRepository.save(productCategory);
    }

    public void deleteProductCategory(UUID id) {
        productCategoryRepository.deleteById(id);
    }

    public ProductCategory getProductsForProductCategory(UUID id) {
        return productCategoryRepository.findById(id).orElseThrow();
    }

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }
}
