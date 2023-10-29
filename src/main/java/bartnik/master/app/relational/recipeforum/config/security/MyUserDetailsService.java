package bartnik.master.app.relational.recipeforum.config.security;

import bartnik.master.app.relational.recipeforum.config.security.MyUserDetails;
import bartnik.master.app.relational.recipeforum.model.CustomUser;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUser> userByUsername = customUserRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        return new MyUserDetails(userByUsername.get());

    }
}
