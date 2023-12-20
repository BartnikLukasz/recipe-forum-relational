package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.dto.request.UpdateProductRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @Column(length = 65535, columnDefinition = "longtext")
    private String description;

    @Column(precision = 5, scale = 2)
    private BigDecimal price;

    @Builder.Default
    private Integer availability = 0;

    @OneToMany(mappedBy = "product")
    private List<LineItem> lineItems;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    public void apply(UpdateProductRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.availability = request.getAvailability();
        this.price = request.getPrice();
    }

}
