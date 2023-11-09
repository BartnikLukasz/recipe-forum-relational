package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.OrderProductsRequest;
import bartnik.master.app.relational.recipeforum.model.CustomUser;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepositoryCrud;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final CustomUserRepository customUserRepository;
    private final CustomUserRepositoryCrud customUserRepositoryCrud;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomUser create(CustomUser user) {
        return customUserRepository.save(user);
    }

    public CustomUser addUser() {
        CustomUser user = CustomUser.builder()
                .username("user_admin")
                .password(passwordEncoder.encode("password"))
                .emailAddress("user@example.com")
                .authorities("ROLE_ADMIN")
                .build();
        return create(user);
    }

    public List<Recipe> getRecommendations(Integer size) {
        var currentUser = UserUtil.getCurrentUser();
        var user = customUserRepository.getByUsername(currentUser.getUsername());
        return customUserRepositoryCrud.getRecommendations(size, user.getId());
    }

    public void buyProducts(OrderProductsRequest request) {

    }
}
