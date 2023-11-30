package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.dto.request.UpdateRecipeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Document("Recipe")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String ingredients;

    @NotBlank
    private String tags;

    @Builder.Default
    Integer numberOfLikes = 0;

    @Builder.Default
    Integer numberOfDislikes = 0;

    @Builder.Default
    private LocalDate created = LocalDate.now();

    @DocumentReference
    private Category category;

    @DocumentReference
    private CustomUser user;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<CustomUser> likedByUsers = new HashSet<CustomUser>();

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<CustomUser> dislikedByUsers = new HashSet<CustomUser>();

    public void apply(UpdateRecipeRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.ingredients = request.getIngredients();
        this.tags = request.getTags();
    }
}
