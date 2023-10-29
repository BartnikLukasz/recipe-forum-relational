package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
}
