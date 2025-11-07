package com.hust.booking.dto;


import com.hust.booking.entity.GroupMember;
import com.hust.booking.enums.MemberRole;
import com.hust.booking.enums.MemberStatus;
import java.time.LocalDateTime;

public class GroupMemberResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private String userFullName;
    private Long groupId;
    private String groupName;
    private MemberStatus status;
    private MemberRole role;
    private LocalDateTime joinedAt;
    private LocalDateTime updatedAt;

    // Constructors
    public GroupMemberResponse() {}

    public GroupMemberResponse(GroupMember groupMember) {
        this.id = groupMember.getId();
        this.userId = groupMember.getUserId();
        this.groupId = groupMember.getGroupId();
        this.status = groupMember.getStatus();
        this.role = groupMember.getRole();
        this.joinedAt = groupMember.getJoinedAt();
        this.updatedAt = groupMember.getUpdatedAt();

        if (groupMember.getUser() != null) {
            this.userEmail = groupMember.getUser().getEmail();
            this.userFullName = groupMember.getUser().getFullName();
        }

        if (groupMember.getGroup() != null) {
            this.groupName = groupMember.getGroup().getName();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }

    public MemberRole getRole() { return role; }
    public void setRole(MemberRole role) { this.role = role; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}