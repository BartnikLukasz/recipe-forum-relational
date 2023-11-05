package bartnik.master.app.relational.Productforum.controller;

import bartnik.master.app.relational.recipeforum.dto.request.CreateProductRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateProductRequest;
import bartnik.master.app.relational.recipeforum.dto.response.ProductDetailsResponse;
import bartnik.master.app.relational.recipeforum.dto.response.ProductResponse;
import bartnik.master.app.relational.recipeforum.mapper.ProductMapper;
import bartnik.master.app.relational.recipeforum.service.ProductService;
import bartnik.master.app.relational.recipeforum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Product")
public class ProductController {

    private final UserService userService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody  @Validated CreateProductRequest request) {
        return ResponseEntity.ok(productMapper.map(productService.createProduct(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productMapper.map(productService.getProductById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody @Validated UpdateProductRequest request) {
        return ResponseEntity.ok(productMapper.map(productService.updateProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productMapper.map(productService.getAllProducts()));
    }
}
