package bartnik.master.app.relational.recipeforum.mapper;

import bartnik.master.app.relational.recipeforum.dto.response.UserResponse;
import bartnik.master.app.relational.recipeforum.model.CustomUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse map(CustomUser user);
}
