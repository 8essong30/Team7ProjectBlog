package com.sparta.blog.category;

import com.sparta.blog.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping("/api/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all category", description = "View all category list Page")
    public List<CategoryResponse> readAll() {
        return categoryService.readAll();
    }
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/api/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create category Page")
    public ResponseEntity<String> createParent(@RequestBody CategoryRequest req) {
            categoryService.createParentCategory(req);
            return new ResponseEntity<>("Create parent category ", HttpStatus.CREATED);
    }

    @PostMapping("/api/categories/{categoryParentId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create category Page")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> createChildren(@PathVariable("categoryParentId") long id, @RequestBody CategoryRequest req) {
        categoryService.createChildrenCategory(id, req);
        return new ResponseEntity<>("Create children category ", HttpStatus.CREATED);

    }

    @DeleteMapping("/api/categories/{id}")
    @Operation(summary = "Delete category",description = "Delete category Page")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>("Create children category ", HttpStatus.OK);

    }
}