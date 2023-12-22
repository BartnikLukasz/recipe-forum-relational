package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.model.Recipe;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.RecipeRepository;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import bartnik.master.app.relational.recipeforum.util.UuidConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final CustomUserRepository customUserRepository;
    private final RecipeRepository recipeRepository;
    private final UuidConverter uuidConverter;

    public Set<Recipe> getCustomRecommendations(Integer size) {
        var currentUser = UserUtil.getCurrentUser();
        var user = customUserRepository.getByUsername(currentUser.getUsername());
        var recommendedIds = customUserRepository.getRecommendations(user.getId().toString(), size).stream()
                .map(bytes -> uuidConverter.convert(bytes)).toList();
        return new HashSet<>(recipeRepository.findAllById(recommendedIds));
    }
}
