package bartnik.master.app.relational.recipeforum.mapper;

import bartnik.master.app.relational.recipeforum.dto.response.CategoryLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.CategoryResponse;
import bartnik.master.app.relational.recipeforum.dto.response.CommentResponse;
import bartnik.master.app.relational.recipeforum.model.Category;
import bartnik.master.app.relational.recipeforum.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Named("recipe")
    @Mapping(target = "username", source = "user.username")
    CommentResponse map(Comment comment);
}
