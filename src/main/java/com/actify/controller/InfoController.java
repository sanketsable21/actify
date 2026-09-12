package com.actify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class InfoController {

    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("app", "Actify API");
        info.put("status", "running");
        info.put("version", "1.0.0");
        info.put("description", "Task Management REST API with JWT Auth + RBAC");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("POST /api/auth/register", "Register new user");
        endpoints.put("POST /api/auth/login", "Login, returns JWT");
        endpoints.put("GET  /api/admin/users", "Admin: list users");
        endpoints.put("GET  /api/manager/users", "Manager: list users");
        endpoints.put("GET  /api/user/profile", "User: own profile");
        info.put("endpoints", endpoints);

        info.put("docs", "Use Postman or curl. Login first to get a JWT token.");
        info.put("github", "https://github.com/sanketsable21/actify");

        return info;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}