package bartnik.master.app.relational.recipeforum.controller;

import bartnik.master.app.relational.recipeforum.dto.request.CreateCategoryRequest;
import bartnik.master.app.relational.recipeforum.dto.response.CategoryResponse;
import bartnik.master.app.relational.recipeforum.mapper.CategoryMapper;
import bartnik.master.app.relational.recipeforum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Validated CreateCategoryRequest request) {
        return ResponseEntity.ok(mapper.map(categoryService.createCategory(request)));
    }

}
