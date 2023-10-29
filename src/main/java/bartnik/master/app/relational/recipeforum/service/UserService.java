package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.model.CustomUser;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final CustomUserRepository customUserRepository;
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
}
