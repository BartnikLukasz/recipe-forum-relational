package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.dto.request.UpdateProductRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Document("Product")
public class Product {

    @Id
    private UUID id;

    @NotBlank
    private String name;

    private String description;

    private BigDecimal price;

    @Builder.Default
    private Integer availability = 0;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private List<LineItem> lineItems;

    @DocumentReference
    private ProductCategory productCategory;

    public void apply(UpdateProductRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.availability = request.getAvailability();
        this.price = request.getPrice();
    }

}
