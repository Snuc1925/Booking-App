package com.hust.booking.repository;

import com.hust.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByRefreshToken(String refreshToken);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    // Find users by name pattern
    List<User> findByFullNameContainingIgnoreCase(String name);

    // Find recently registered users
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date ORDER BY u.createdAt DESC")
    List<User> findRecentlyRegisteredUsers(@Param("date") LocalDateTime date);

    // Count total users
    @Query("SELECT COUNT(u) FROM User u")
    Long countTotalUsers();

    // Update user profile
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.fullName = :fullName, u.phone = :phone, u.updatedAt = :updatedAt WHERE u.id = :id")
    void updateUserProfile(@Param("id") Long id,
                           @Param("fullName") String fullName,
                           @Param("phone") String phone,
                           @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.refreshToken = :refreshToken, u.refreshTokenExpiresAt = :expiresAt WHERE u.id = :userId")
    void updateRefreshToken(@Param("userId") Long userId,
                            @Param("refreshToken") String refreshToken,
                            @Param("expiresAt") LocalDateTime expiresAt);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.refreshToken = null, u.refreshTokenExpiresAt = null WHERE u.id = :userId")
    void clearRefreshToken(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.refreshToken = null, u.refreshTokenExpiresAt = null WHERE u.refreshTokenExpiresAt < :now")
    void clearExpiredRefreshTokens(@Param("now") LocalDateTime now);
}