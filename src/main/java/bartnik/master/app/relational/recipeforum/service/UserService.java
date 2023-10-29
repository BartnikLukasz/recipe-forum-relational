package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.config.security.CustomOAuth2User;
import bartnik.master.app.relational.recipeforum.model.CustomUser;
import bartnik.master.app.relational.recipeforum.model.enums.Provider;
import bartnik.master.app.relational.recipeforum.repository.AuthorityRepository;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final CustomUserRepository customUserRepository;
    private final AuthorityRepository authorityRepository;

    public CustomUser create(CustomUser user) {
        return customUserRepository.save(user);
    }

    public void processOAuthPostLogin(String username) {
        System.out.println("username: " + username);
        var existUser = customUserRepository.findByUsername(username);

        var authority = authorityRepository.findAll().get(0);

        if (existUser.isEmpty()) {
            CustomUser newUser = new CustomUser();
            newUser.setUsername(username);
            newUser.setProvider(Provider.GITHUB);
            newUser.setEnabled(true);
            newUser.setAuthorities(Set.of(authority));

            customUserRepository.save(newUser);
        }

    }

    public CustomUser editUser(CustomOAuth2User principal) {
        var user = customUserRepository.findByUsername(principal.getName()).get();

        user.setEmail(LocalDateTime.now().toString());

        return customUserRepository.save(user);
    }
}
