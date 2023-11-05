package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.dto.request.UpdateRecipeRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @NotBlank
    private String name;

    private String description;

    @Column(precision = 4, scale = 2)
    private BigDecimal price;

    @Builder.Default
    private Integer availability = 0;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    public void apply(UpdateRecipeRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.availability = request.getAvailability();
        this.price = request.getPrice();
    }

}
