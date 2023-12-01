package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.config.UuidIdentifiedEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document("Comment")
public class Comment extends UuidIdentifiedEntity {

    @NotBlank
    private String content;

    @Builder.Default
    private LocalDate created = LocalDate.now();

    @DocumentReference
    private CustomUser user;

    @DocumentReference
    private Recipe recipe;
}
