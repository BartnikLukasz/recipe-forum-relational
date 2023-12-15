package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.dto.request.UpdateRecipeRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "recipe")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @NotBlank
    private String title;

    @Column(length = 65535)
    @NotBlank
    private String content;

    @Column(length = 65535)
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;

    @OneToMany(mappedBy = "recipe")
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "likedRecipes")
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<CustomUser> likedByUsers = new HashSet<CustomUser>();

    @ManyToMany(mappedBy = "dislikedRecipes")
    private Set<CustomUser> dislikedByUsers = new HashSet<CustomUser>();

    public void apply(UpdateRecipeRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.ingredients = request.getIngredients();
        this.tags = request.getTags();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Recipe recipe = (Recipe) o;
        return getId() != null && Objects.equals(getId(), recipe.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
