package bartnik.master.app.relational.recipeforum.controller;

import bartnik.master.app.relational.recipeforum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/")
public class RecipeController {

    private final UserService userService;

    @GetMapping("")
    public String getEmpty() {
        return "empty";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/recipe")
    public String getRecipes() {
        return "recipes";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/addUser")
    public String addUser() {
        return userService.addUser().toString();
    }
}
