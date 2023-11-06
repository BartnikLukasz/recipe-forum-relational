package bartnik.master.app.relational.recipeforum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name="custom_user")
@AllArgsConstructor
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;


    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    private String emailAddress;

    private String authorities;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Recipe> recipes;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Comment> comments;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "custom_users_liked_recipes",
            joinColumns = { @JoinColumn(name = "custom_user_id")},
            inverseJoinColumns = { @JoinColumn(name = "liked_recipe_id")}
    )
    @ToString.Exclude
    private Set<Recipe> likedRecipes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "custom_users_disliked_recipes",
            joinColumns = { @JoinColumn(name = "custom_user_id")},
            inverseJoinColumns = { @JoinColumn(name = "disliked_recipe_id")}
    )
    @ToString.Exclude
    private Set<Recipe> dislikedRecipes;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CustomUser that = (CustomUser) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
