package com.hust.booking.service;

import com.hust.booking.dto.*;
import com.hust.booking.entity.User;
import com.hust.booking.exception.EmailAlreadyExistsException;
import com.hust.booking.exception.PhoneAlreadyExistsException;
import com.hust.booking.exception.InvalidRefreshTokenException;
import com.hust.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateSecureRefreshToken();

        // Save refresh token to database
        userRepository.updateRefreshToken(
                user.getId(),
                refreshToken,
                jwtService.getRefreshTokenExpiryDate()
        );

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                jwtService.getAccessTokenExpirationSeconds()
        );
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use!");
        }

        // Check if phone already exists
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number is already in use!");
        }

        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setFullName(registerRequest.getFullName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        return new RegisterResponse("Đăng ký thành công");
    }

    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Find user by refresh token
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        // Check if refresh token is expired
        if (user.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())) {
            userRepository.clearRefreshToken(user.getId());
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateSecureRefreshToken();

        // Update refresh token in database
        userRepository.updateRefreshToken(
                user.getId(),
                newRefreshToken,
                jwtService.getRefreshTokenExpiryDate()
        );

        return new TokenResponse(
                newAccessToken,
                newRefreshToken,
                jwtService.getAccessTokenExpirationSeconds()
        );
    }

    @Transactional
    public void logout(Long userId) {
        userRepository.clearRefreshToken(userId);
    }

    @Transactional
    public void cleanupExpiredTokens() {
        userRepository.clearExpiredRefreshTokens(LocalDateTime.now());
    }
}