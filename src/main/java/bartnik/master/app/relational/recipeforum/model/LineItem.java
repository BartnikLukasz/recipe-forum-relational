package bartnik.master.app.relational.recipeforum.model;

import bartnik.master.app.relational.recipeforum.config.UuidIdentifiedEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.UUID;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@Document("LineItem")
@AllArgsConstructor
public class LineItem extends UuidIdentifiedEntity {

    @DocumentReference
    private Product product;

    @DocumentReference
    private Order order;

    private Integer quantity;
}
