package bartnik.master.app.relational.recipeforum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @OneToMany
    @ToString.Exclude
    @Builder.Default
    private Set<Product> products = new HashSet<>();
}
