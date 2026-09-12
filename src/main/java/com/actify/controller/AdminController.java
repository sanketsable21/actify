package com.actify.controller;

import com.actify.dto.AssignRoleRequest;
import com.actify.dto.RegisterRequest;
import com.actify.dto.UserResponse;
import com.actify.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET one user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // CREATE a user
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // UPDATE a user
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // DELETE a user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ASSIGN a role to a user
    @PostMapping("/assign-role")
    public ResponseEntity<UserResponse> assignRole(
            @Valid @RequestBody AssignRoleRequest request) {
        return ResponseEntity.ok(userService.assignRole(request.getUserId(), request.getRoleName()));
    }
}