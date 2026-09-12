package com.actify.controller;

import com.actify.dto.TaskResponse;
import com.actify.dto.UserResponse;
import com.actify.service.TaskService;
import com.actify.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getMyTasks(Authentication authentication) {
        String email = authentication.getName();
        UserResponse me = userService.getUserByEmail(email);
        return ResponseEntity.ok(taskService.getTasksForUser(me.getId()));
    }
}