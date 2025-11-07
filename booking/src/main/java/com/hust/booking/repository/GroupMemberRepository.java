package com.hust.booking.repository;


import com.hust.booking.entity.GroupMember;
import com.hust.booking.enums.MemberRole;
import com.hust.booking.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Optional<GroupMember> findByUserIdAndGroupId(Long userId, Long groupId);

    List<GroupMember> findByGroupIdAndStatus(Long groupId, MemberStatus status);

    List<GroupMember> findByUserIdAndStatus(Long userId, MemberStatus status);

    List<GroupMember> findByGroupId(Long groupId);

    List<GroupMember> findByUserId(Long userId);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT gm FROM GroupMember gm WHERE gm.groupId = :groupId AND gm.role = :role")
    List<GroupMember> findByGroupIdAndRole(@Param("groupId") Long groupId, @Param("role") MemberRole role);

    @Query("SELECT COUNT(gm) FROM GroupMember gm WHERE gm.groupId = :groupId AND gm.status = :status")
    Long countByGroupIdAndStatus(@Param("groupId") Long groupId, @Param("status") MemberStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE GroupMember gm SET gm.status = :status WHERE gm.id = :id")
    void updateMemberStatus(@Param("id") Long id, @Param("status") MemberStatus status);

    @Modifying
    @Transactional
    @Query("DELETE FROM GroupMember gm WHERE gm.userId = :userId AND gm.groupId = :groupId")
    void removeUserFromGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);
}