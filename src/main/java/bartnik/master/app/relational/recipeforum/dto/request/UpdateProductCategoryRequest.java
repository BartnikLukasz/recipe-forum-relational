package bartnik.master.app.relational.recipeforum.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class UpdateProductCategoryRequest {

    @NotBlank
    String name;

}
