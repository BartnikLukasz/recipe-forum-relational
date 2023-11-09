package bartnik.master.app.relational.recipeforum.controller;

import bartnik.master.app.relational.recipeforum.dto.response.RecipeLiteResponse;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeResponse;
import bartnik.master.app.relational.recipeforum.mapper.RecipeMapper;
import bartnik.master.app.relational.recipeforum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RecipeMapper recipeMapper;

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecipeLiteResponse>> getRecommendations(@RequestParam("size") Integer size) {
        return ResponseEntity.ok(recipeMapper.mapLite(userService.getRecommendations(size)));
    }
}
