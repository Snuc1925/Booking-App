package com.hust.booking.service;

import com.hust.booking.dto.*;
import com.hust.booking.entity.Group;
import com.hust.booking.entity.GroupMember;
import com.hust.booking.entity.User;
import com.hust.booking.enums.MemberRole;
import com.hust.booking.enums.MemberStatus;
import com.hust.booking.exception.GroupNotFoundException;
import com.hust.booking.exception.UserAlreadyInGroupException;
import com.hust.booking.exception.UserNotInGroupException;
import com.hust.booking.exception.UnauthorizedGroupActionException;
import com.hust.booking.repository.GroupRepository;
import com.hust.booking.repository.GroupMemberRepository;
import com.hust.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public GroupResponse createGroup(CreateGroupRequest request, Long ownerId) {
        // Generate unique group code
        String code = generateUniqueGroupCode();

        // Create new group
        Group group = new Group(code, request.getName());
        Group savedGroup = groupRepository.save(group);

        // Add creator as owner
        GroupMember owner = new GroupMember(ownerId, savedGroup.getId(), MemberStatus.ACCEPTED, MemberRole.OWNER);
        groupMemberRepository.save(owner);

        return new GroupResponse(savedGroup);
    }

    @Transactional
    public GroupMemberResponse joinGroupByCode(JoinGroupRequest request, Long userId) {
        // Find group by code
        Group group = groupRepository.findByCode(request.getCode())
                .orElseThrow(() -> new GroupNotFoundException("Group not found with code: " + request.getCode()));

        // Check if user is already in group
        if (groupMemberRepository.existsByUserIdAndGroupId(userId, group.getId())) {
            throw new UserAlreadyInGroupException("User is already a member of this group");
        }

        // Add user to group with pending status
        GroupMember groupMember = new GroupMember(userId, group.getId(), MemberStatus.PENDING, MemberRole.MEMBER);
        GroupMember savedMember = groupMemberRepository.save(groupMember);

        return new GroupMemberResponse(savedMember);
    }

    @Transactional
    public GroupMemberResponse addMemberToGroup(Long groupId, AddMemberRequest request, Long requesterId) {
        // Verify requester is owner or member of the group
        validateUserGroupPermission(requesterId, groupId);

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // Check if user is already in group
        if (groupMemberRepository.existsByUserIdAndGroupId(user.getId(), groupId)) {
            throw new UserAlreadyInGroupException("User is already a member of this group");
        }

        // Add user to group with pending status
        GroupMember groupMember = new GroupMember(user.getId(), groupId, MemberStatus.PENDING, MemberRole.MEMBER);
        GroupMember savedMember = groupMemberRepository.save(groupMember);

        return new GroupMemberResponse(savedMember);
    }

    public List<GroupResponse> getUserGroups(Long userId) {
        List<Group> groups = groupRepository.findGroupsByUserId(userId);
        return groups.stream()
                .map(group -> {
                    Long memberCount = groupRepository.countAcceptedMembersByGroupId(group.getId());
                    return new GroupResponse(group, memberCount);
                })
                .collect(Collectors.toList());
    }

    public List<GroupResponse> getGroupsOwnedByUser(Long userId) {
        List<Group> groups = groupRepository.findGroupsOwnedByUser(userId);
        return groups.stream()
                .map(group -> {
                    Long memberCount = groupRepository.countAcceptedMembersByGroupId(group.getId());
                    return new GroupResponse(group, memberCount);
                })
                .collect(Collectors.toList());
    }

    public GroupResponse getGroupById(Long groupId, Long userId) {
        // Verify user has access to this group
        validateUserGroupPermission(userId, groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + groupId));

        Long memberCount = groupRepository.countAcceptedMembersByGroupId(groupId);
        return new GroupResponse(group, memberCount);
    }

    public GroupResponse getGroupByCode(String code) {
        Group group = groupRepository.findByCode(code)
                .orElseThrow(() -> new GroupNotFoundException("Group not found with code: " + code));

        Long memberCount = groupRepository.countAcceptedMembersByGroupId(group.getId());
        return new GroupResponse(group, memberCount);
    }

    public List<GroupMemberResponse> getGroupMembers(Long groupId, Long requesterId) {
        // Verify requester has access to this group
        validateUserGroupPermission(requesterId, groupId);

        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        return members.stream()
                .map(GroupMemberResponse::new)
                .collect(Collectors.toList());
    }

    public List<GroupMemberResponse> getUserMemberships(Long userId) {
        List<GroupMember> memberships = groupMemberRepository.findByUserId(userId);
        return memberships.stream()
                .map(GroupMemberResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMemberStatus(Long groupId, Long memberId, UpdateMemberStatusRequest request, Long requesterId) {
        // Verify requester is owner of the group
        validateUserIsGroupOwner(requesterId, groupId);

        GroupMember member = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotInGroupException("Member not found"));

        if (!member.getGroupId().equals(groupId)) {
            throw new UserNotInGroupException("Member does not belong to this group");
        }

        groupMemberRepository.updateMemberStatus(memberId, request.getStatus());
    }

    @Transactional
    public void removeUserFromGroup(Long groupId, Long userId, Long requesterId) {
        // User can remove themselves, or owner can remove others
        if (!userId.equals(requesterId)) {
            validateUserIsGroupOwner(requesterId, groupId);
        }

        if (!groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new UserNotInGroupException("User is not a member of this group");
        }

        groupMemberRepository.removeUserFromGroup(userId, groupId);
    }

    @Transactional
    public void deleteGroup(Long groupId, Long requesterId) {
        // Verify requester is owner of the group
        validateUserIsGroupOwner(requesterId, groupId);

        // Delete all group members first
        groupMemberRepository.deleteAll(groupMemberRepository.findByGroupId(groupId));

        // Delete the group
        groupRepository.deleteById(groupId);
    }

    private String generateUniqueGroupCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (groupRepository.existsByCode(code));
        return code;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    private void validateUserGroupPermission(Long userId, Long groupId) {
        if (!groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new UnauthorizedGroupActionException("User does not have permission to access this group");
        }
    }

    private void validateUserIsGroupOwner(Long userId, Long groupId) {
        GroupMember member = groupMemberRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UnauthorizedGroupActionException("User is not a member of this group"));

        if (member.getRole() != MemberRole.OWNER) {
            throw new UnauthorizedGroupActionException("Only group owners can perform this action");
        }
    }
}