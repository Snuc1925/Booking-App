package com.hust.booking.repository;

import com.hust.booking.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByCode(String code);

    boolean existsByCode(String code);

    List<Group> findByNameContainingIgnoreCase(String name);

    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.userId = :userId AND m.status = 'ACCEPTED'")
    List<Group> findGroupsByUserId(@Param("userId") Long userId);

    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.userId = :userId AND m.role = 'OWNER'")
    List<Group> findGroupsOwnedByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM GroupMember m WHERE m.groupId = :groupId AND m.status = 'ACCEPTED'")
    Long countAcceptedMembersByGroupId(@Param("groupId") Long groupId);
}