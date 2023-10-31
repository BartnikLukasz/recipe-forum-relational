package bartnik.master.app.relational.recipeforum.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Validated
public class CategoryResponse extends CategoryLiteResponse {

    @Builder.Default
    Set<RecipeResponse> recipes = new HashSet<>();
}
