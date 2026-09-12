package com.actify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "roleName is required (ADMIN, MANAGER, or USER)")
    private String roleName;
}