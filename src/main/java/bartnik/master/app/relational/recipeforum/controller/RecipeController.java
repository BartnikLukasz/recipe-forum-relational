package bartnik.master.app.relational.recipeforum.controller;

import bartnik.master.app.relational.recipeforum.config.security.CustomOAuth2User;
import bartnik.master.app.relational.recipeforum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/recipe")
    public String getRecipes(Authentication authentication) {

        return "recipes for " + userService.editUser((CustomOAuth2User) authentication.getPrincipal()).toString();
    }
}
