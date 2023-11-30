package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.CreateProductRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateProductRequest;
import bartnik.master.app.relational.recipeforum.model.Product;
import bartnik.master.app.relational.recipeforum.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) {
        var productCategory = productCategoryRepository.findById(request.getProductCategory()).orElseThrow();

        var product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .availability(request.getAvailability())
                .productCategory(productCategory)
                .build();

        return productRepository.save(product);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(UUID id, UpdateProductRequest request) {
        var product = productRepository.findById(id).orElseThrow();
        var productCategory = product.getProductCategory();

        if (!product.getProductCategory().getId().equals(request.getProductCategory())) {
            productCategory = productCategoryRepository.findById(request.getProductCategory()).orElseThrow();
            product.setProductCategory(productCategory);
        }
        product.apply(request);

        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
