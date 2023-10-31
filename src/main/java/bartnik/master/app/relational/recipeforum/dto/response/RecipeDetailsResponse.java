package bartnik.master.app.relational.recipeforum.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Validated
public class RecipeDetailsResponse extends RecipeResponse {

    @NotNull
    CategoryLiteResponse category;

    @NotNull
    UserResponse user;
}
