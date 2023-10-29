package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.model.enums.Provider;
import jakarta.persistence.*;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;


    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    private String password;

    private String email;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities", joinColumns = {
            @JoinColumn(name = "USERS_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
            @JoinColumn(name = "AUTHORITIES_ID", referencedColumnName = "ID") })
    private Set<Authority> authorities;

    @OneToMany
    @ToString.Exclude
    private Set<Recipe> recipes;

    @OneToMany
    @ToString.Exclude
    private Set<Recipe> likedRecipes;

    @OneToMany
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
