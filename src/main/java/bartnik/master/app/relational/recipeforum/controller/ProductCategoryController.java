package bartnik.master.app.relational.recipeforum.controller;

import bartnik.master.app.relational.recipeforum.dto.request.CreateProductCategoryRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateProductCategoryRequest;
import bartnik.master.app.relational.recipeforum.dto.response.ProductCategoryLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.ProductCategoryResponse;
import bartnik.master.app.relational.recipeforum.mapper.ProductCategoryMapper;
import bartnik.master.app.relational.recipeforum.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    private final ProductCategoryMapper mapper;

    @PostMapping
    public ResponseEntity<ProductCategoryLiteResponse> createProductCategory(@RequestBody @Validated CreateProductCategoryRequest request) {
        return ResponseEntity.ok(mapper.map(productCategoryService.createProductCategory(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryLiteResponse> updateProductCategory(@PathVariable UUID id, @RequestBody @Validated UpdateProductCategoryRequest request) {
        return ResponseEntity.ok(mapper.map(productCategoryService.updateProductCategory(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductCategoryLiteResponse> deleteProductCategory(@PathVariable UUID id) {
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> getProductsForProductCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.map(productCategoryService.getRecipesForProductCategory(id)));
    }

}
