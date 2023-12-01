package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.config.UuidIdentifiedEntity;
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
@EqualsAndHashCode
@Document("Category")
public class Category extends UuidIdentifiedEntity {

    @NotBlank
    private String name;

    @Builder.Default
    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<Recipe> recipes = new HashSet<>();
}
