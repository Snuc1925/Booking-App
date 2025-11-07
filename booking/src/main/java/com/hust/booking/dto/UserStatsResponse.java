package com.hust.booking.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserStatsResponse {

    private Long totalUsers;
    private List<UserProfileResponse> recentUsers;
    private LocalDateTime generatedAt;

    // Constructors
    public UserStatsResponse() {}

    public UserStatsResponse(Long totalUsers, List<UserProfileResponse> recentUsers) {
        this.totalUsers = totalUsers;
        this.recentUsers = recentUsers;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public List<UserProfileResponse> getRecentUsers() { return recentUsers; }
    public void setRecentUsers(List<UserProfileResponse> recentUsers) { this.recentUsers = recentUsers; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}