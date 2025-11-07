package com.hust.booking.controller;

import com.hust.booking.dto.*;
import com.hust.booking.entity.User;
import com.hust.booking.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * Create a new group
     */
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        GroupResponse group = groupService.createGroup(request, currentUser.getId());
        return ResponseEntity.ok(group);
    }

    /**
     * Join group by code
     */
    @PostMapping("/join")
    public ResponseEntity<GroupMemberResponse> joinGroup(@Valid @RequestBody JoinGroupRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        GroupMemberResponse membership = groupService.joinGroupByCode(request, currentUser.getId());
        return ResponseEntity.ok(membership);
    }

    /**
     * Get group by code (for preview before joining)
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<GroupResponse> getGroupByCode(@PathVariable String code) {
        GroupResponse group = groupService.getGroupByCode(code);
        return ResponseEntity.ok(group);
    }

    /**
     * Get current user's groups
     */
    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupResponse>> getMyGroups() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<GroupResponse> groups = groupService.getUserGroups(currentUser.getId());
        return ResponseEntity.ok(groups);
    }

    /**
     * Get groups owned by current user
     */
    @GetMapping("/owned")
    public ResponseEntity<List<GroupResponse>> getOwnedGroups() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<GroupResponse> groups = groupService.getGroupsOwnedByUser(currentUser.getId());
        return ResponseEntity.ok(groups);
    }

    /**
     * Get group details by ID
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        GroupResponse group = groupService.getGroupById(groupId, currentUser.getId());
        return ResponseEntity.ok(group);
    }

    /**
     * Get group members
     */
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> getGroupMembers(@PathVariable Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<GroupMemberResponse> members = groupService.getGroupMembers(groupId, currentUser.getId());
        return ResponseEntity.ok(members);
    }

    /**
     * Add member to group by email
     */
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupMemberResponse> addMember(
            @PathVariable Long groupId,
            @Valid @RequestBody AddMemberRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        GroupMemberResponse member = groupService.addMemberToGroup(groupId, request, currentUser.getId());
        return ResponseEntity.ok(member);
    }

    /**
     * Update member status (accept/reject pending members)
     */
    @PutMapping("/{groupId}/members/{memberId}/status")
    public ResponseEntity<Map<String, String>> updateMemberStatus(
            @PathVariable Long groupId,
            @PathVariable Long memberId,
            @Valid @RequestBody UpdateMemberStatusRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        groupService.updateMemberStatus(groupId, memberId, request, currentUser.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Member status updated successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Remove member from group (or leave group)
     */
    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Map<String, String>> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        groupService.removeUserFromGroup(groupId, userId, currentUser.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Member removed successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Leave group
     */
    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Map<String, String>> leaveGroup(@PathVariable Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        groupService.removeUserFromGroup(groupId, currentUser.getId(), currentUser.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Left group successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Delete group (owners only)
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Map<String, String>> deleteGroup(@PathVariable Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        groupService.deleteGroup(groupId, currentUser.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Group deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user's group memberships
     */
    @GetMapping("/memberships")
    public ResponseEntity<List<GroupMemberResponse>> getMyMemberships() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<GroupMemberResponse> memberships = groupService.getUserMemberships(currentUser.getId());
        return ResponseEntity.ok(memberships);
    }
}