package bartnik.master.app.relational.recipeforum.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Document("ProductCategory")
public class ProductCategory {

    @Id
    private UUID id;

    @NotBlank
    private String name;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Product> products = new HashSet<>();
}
