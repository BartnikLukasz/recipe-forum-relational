package bartnik.master.app.relational.recipeforum.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@Document("CustomUser")
@AllArgsConstructor
public class CustomUser {

    @Id
    private UUID id;


    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String emailAddress;

    private String authorities;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<Recipe> recipes;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private List<Comment> comments;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private List<Order> orders;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<Recipe> likedRecipes;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<Recipe> dislikedRecipes;
}
