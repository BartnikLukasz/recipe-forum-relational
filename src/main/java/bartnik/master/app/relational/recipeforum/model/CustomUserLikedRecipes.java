package bartnik.master.app.relational.recipeforum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity(name = "custom_users_liked_recipes")
public class CustomUserLikedRecipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "custom_user_id")
    private UUID userId;

    @Column(name = "liked_recipe_id")
    private UUID recipeId;


}
