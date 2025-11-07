package com.hust.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JoinGroupRequest {

    @NotBlank(message = "Group code is required")
    @Size(min = 6, max = 6, message = "Group code must be exactly 6 characters")
    private String code;

    // Constructors
    public JoinGroupRequest() {}

    public JoinGroupRequest(String code) {
        this.code = code;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}