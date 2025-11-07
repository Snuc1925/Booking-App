package com.hust.booking.dto;

import com.hust.booking.enums.MemberStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateMemberStatusRequest {

    @NotNull(message = "Status is required")
    private MemberStatus status;

    // Constructors
    public UpdateMemberStatusRequest() {}

    public UpdateMemberStatusRequest(MemberStatus status) {
        this.status = status;
    }

    // Getters and Setters
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
}