package bartnik.master.app.relational.recipeforum.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Validated
public class RecipeResponse {

    @NotNull
    UUID id;

    @NotBlank
    String title;

    @NotBlank
    String content;

    @NotBlank
    String ingredients;

    @NotBlank
    String tags;

    Integer numberOfLikes;

    Integer numberOfDislikes;

}
