package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.CustomUser;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomUserRepository extends MongoRepository<CustomUser, UUID> {
    Optional<CustomUser> findByUsername(String username);
    default CustomUser getByUsername(String username) {
        return findByUsername(username).orElseThrow();
    }
}
