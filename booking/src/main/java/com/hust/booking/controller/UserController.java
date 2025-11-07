package com.hust.booking.controller;

import com.hust.booking.dto.ChangePasswordRequest;
import com.hust.booking.dto.UpdateProfileRequest;
import com.hust.booking.dto.UserProfileResponse;
import com.hust.booking.dto.UserStatsResponse;
import com.hust.booking.entity.User;
import com.hust.booking.service.UserService;
import com.hust.booking.util.SecurityContextHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get current user's profile
     * This endpoint demonstrates JWT authentication working
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        UserProfileResponse userProfile = userService.getUserById(currentUser.getId());
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Get user profile by ID (admin or self only)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHelper.getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Users can only access their own profile unless they're admin
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserProfileResponse userProfile = userService.getUserById(id);
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Get all users with pagination
     * Protected endpoint - requires authentication
     */
    @GetMapping
    public ResponseEntity<Page<UserProfileResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        Page<UserProfileResponse> users = userService.getAllUsersWithPagination(page, size, sortBy);
        return ResponseEntity.ok(users);
    }

    /**
     * Search users by name
     * Protected endpoint
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserProfileResponse>> searchUsers(@RequestParam String name) {
        List<UserProfileResponse> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get user statistics
     * Protected endpoint
     */
    @GetMapping("/stats")
    public ResponseEntity<UserStatsResponse> getUserStats() {
        UserStatsResponse stats = userService.getUserStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Update current user's profile
     */
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        UserProfileResponse updatedProfile = userService.updateUserProfile(currentUser.getId(), request);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Change current user's password
     */
    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        userService.changePassword(currentUser.getId(), request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Delete current user's account
     */
    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        userService.deleteUser(currentUser.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Check authentication status
     * This endpoint proves JWT is working by returning authenticated user info
     */
    @GetMapping("/auth-status")
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put("authenticated", true);
        authInfo.put("userId", currentUser.getId());
        authInfo.put("email", currentUser.getEmail());
        authInfo.put("fullName", currentUser.getFullName());
        authInfo.put("authorities", currentUser.getAuthorities());
        authInfo.put("accountNonExpired", currentUser.isAccountNonExpired());
        authInfo.put("accountNonLocked", currentUser.isAccountNonLocked());
        authInfo.put("credentialsNonExpired", currentUser.isCredentialsNonExpired());
        authInfo.put("enabled", currentUser.isEnabled());

        return ResponseEntity.ok(authInfo);
    }

    /**
     * Get current user's account info with sensitive details
     * This proves JWT authentication and authorization are working
     */
    @GetMapping("/me/account-details")
    public ResponseEntity<Map<String, Object>> getAccountDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Map<String, Object> accountDetails = new HashMap<>();
        accountDetails.put("id", currentUser.getId());
        accountDetails.put("email", currentUser.getEmail());
        accountDetails.put("phone", currentUser.getPhone());
        accountDetails.put("fullName", currentUser.getFullName());
        accountDetails.put("createdAt", currentUser.getCreatedAt());
        accountDetails.put("updatedAt", currentUser.getUpdatedAt());
        accountDetails.put("lastLogin", java.time.LocalDateTime.now()); // You can track this
        accountDetails.put("accountStatus", "ACTIVE");
        accountDetails.put("hasRefreshToken", currentUser.getRefreshToken() != null);
        accountDetails.put("refreshTokenExpiry", currentUser.getRefreshTokenExpiresAt());

        return ResponseEntity.ok(accountDetails);
    }
}