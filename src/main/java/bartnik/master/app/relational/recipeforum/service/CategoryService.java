package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.CreateCategoryRequest;
import bartnik.master.app.relational.recipeforum.dto.request.CreateRecipeRequest;
import bartnik.master.app.relational.recipeforum.model.Category;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import bartnik.master.app.relational.recipeforum.repository.CategoryRepository;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.RecipeRepository;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CustomUserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryRequest request) {

        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());

        var category = Category.builder()
                .name(request.getName())
                .build();

        return categoryRepository.save(category);
    }
}
