package bartnik.master.app.relational.recipeforum.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@Document("Orders")
@AllArgsConstructor
public class Order {

    @Id
    private UUID id;

    @DocumentReference
    private CustomUser user;

    @DocumentReference(lazy = true)
    @EqualsAndHashCode.Exclude
    private List<LineItem> items;

    private BigDecimal value;

    private LocalDateTime orderDate;
}
