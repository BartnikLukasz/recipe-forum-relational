package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.model.Recipe;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepositoryCrud;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final CustomUserRepository customUserRepository;
    private final CustomUserRepositoryCrud customUserRepositoryCrud;

    public Set<Recipe> getRecommendations(Integer size) {
        var currentUser = UserUtil.getCurrentUser();
        var user = customUserRepository.getByUsername(currentUser.getUsername());
        return customUserRepositoryCrud.getRecommendations(size, user.getId());
    }
}
