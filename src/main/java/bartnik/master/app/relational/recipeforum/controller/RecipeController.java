package bartnik.master.app.relational.recipeforum.controller;

import bartnik.master.app.relational.recipeforum.dto.request.CreateRecipeRequest;
import bartnik.master.app.relational.recipeforum.dto.request.UpdateRecipeRequest;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeDetailsResponse;
import bartnik.master.app.relational.recipeforum.dto.response.RecipeResponse;
import bartnik.master.app.relational.recipeforum.mapper.RecipeMapper;
import bartnik.master.app.relational.recipeforum.service.RecipeService;
import bartnik.master.app.relational.recipeforum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final UserService userService;
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody  @Validated CreateRecipeRequest request) {
        return ResponseEntity.ok(recipeMapper.map(recipeService.createRecipe(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailsResponse> getRecipe(@PathVariable UUID id) {
        return ResponseEntity.ok(recipeMapper.mapDetails(recipeService.getRecipeById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable UUID id, @RequestBody @Validated UpdateRecipeRequest request) {
        return ResponseEntity.ok(recipeMapper.map(recipeService.updateRecipe(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecipeResponse> deleteRecipe(@PathVariable UUID id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecipeDetailsResponse>> getAllRecipes() {
        return ResponseEntity.ok(recipeMapper.mapDetails(recipeService.getAllRecipes()));
    }

    @PutMapping("/{id}/rate")
    public ResponseEntity<RecipeResponse> rateRecipe(@RequestParam("liked") boolean liked, @PathVariable UUID id) {
        return ResponseEntity.ok(recipeMapper.map(recipeService.rateRecipe(id, liked)));
    }

    @GetMapping("/empty")
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
