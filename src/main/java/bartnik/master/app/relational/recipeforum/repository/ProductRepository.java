package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.model.Product;
import bartnik.master.app.relational.recipeforum.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

}
