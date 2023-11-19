package bartnik.master.app.relational.recipeforum.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Validated
public class RecipeResponse extends RecipeLiteResponse {

    @NotBlank
    String content;

    @NotBlank
    String ingredients;
}
