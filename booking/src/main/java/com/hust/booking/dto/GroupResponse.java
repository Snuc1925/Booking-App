package com.hust.booking.dto;

import com.hust.booking.entity.Group;
import java.time.LocalDateTime;

public class GroupResponse {

    private Long id;
    private String code;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberCount;

    // Constructors
    public GroupResponse() {}

    public GroupResponse(Group group) {
        this.id = group.getId();
        this.code = group.getCode();
        this.name = group.getName();
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
    }

    public GroupResponse(Group group, Long memberCount) {
        this(group);
        this.memberCount = memberCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getMemberCount() { return memberCount; }
    public void setMemberCount(Long memberCount) { this.memberCount = memberCount; }
}
