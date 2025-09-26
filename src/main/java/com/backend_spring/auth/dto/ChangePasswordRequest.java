package com.backend_spring.auth.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {


}
