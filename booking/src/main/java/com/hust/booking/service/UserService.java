package com.hust.booking.service;

import com.hust.booking.dto.ChangePasswordRequest;
import com.hust.booking.dto.UpdateProfileRequest;
import com.hust.booking.dto.UserProfileResponse;
import com.hust.booking.dto.UserStatsResponse;
import com.hust.booking.entity.User;
import com.hust.booking.exception.PhoneAlreadyExistsException;
import com.hust.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserProfileResponse::new)
                .collect(Collectors.toList());
    }

    public Page<UserProfileResponse> getAllUsersWithPagination(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return userRepository.findAll(pageable)
                .map(UserProfileResponse::new);
    }

    public UserProfileResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return new UserProfileResponse(user);
    }

    public UserProfileResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserProfileResponse(user);
    }

    public UserProfileResponse getUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));
        return new UserProfileResponse(user);
    }

    public List<UserProfileResponse> searchUsersByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name)
                .stream()
                .map(UserProfileResponse::new)
                .collect(Collectors.toList());
    }

    public UserStatsResponse getUserStats() {
        Long totalUsers = userRepository.countTotalUsers();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<UserProfileResponse> recentUsers = userRepository.findRecentlyRegisteredUsers(oneWeekAgo)
                .stream()
                .map(UserProfileResponse::new)
                .collect(Collectors.toList());

        return new UserStatsResponse(totalUsers, recentUsers);
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Check if the new phone number is already taken by another user
        if (!user.getPhone().equals(request.getPhone()) &&
                userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number is already in use by another user!");
        }

        userRepository.updateUserProfile(
                userId,
                request.getFullName(),
                request.getPhone(),
                LocalDateTime.now()
        );

        // Fetch updated user
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found after update"));

        return new UserProfileResponse(updatedUser);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public User getCurrentUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}