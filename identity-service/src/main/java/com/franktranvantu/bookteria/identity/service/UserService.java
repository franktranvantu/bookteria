package com.franktranvantu.bookteria.identity.service;

import static com.franktranvantu.bookteria.identity.exception.ServiceStatusCode.USER_EXISTED;
import static com.franktranvantu.bookteria.identity.exception.ServiceStatusCode.USER_NOT_FOUND;

import com.franktranvantu.bookteria.identity.constant.PredefinedRole;
import com.franktranvantu.bookteria.identity.dto.request.UserCreationRequest;
import com.franktranvantu.bookteria.identity.dto.request.UserUpdateRequest;
import com.franktranvantu.bookteria.identity.dto.response.UserResponse;
import com.franktranvantu.bookteria.identity.exception.ServiceException;
import com.franktranvantu.bookteria.identity.mapper.UserMapper;
import com.franktranvantu.bookteria.identity.mapper.UserProfileMapper;
import com.franktranvantu.bookteria.identity.repository.RoleRepository;
import com.franktranvantu.bookteria.identity.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.franktranvantu.bookteria.identity.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    UserProfileMapper userProfileMapper;
    ProfileClient profileClient;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ServiceException(USER_EXISTED);
        }
        final var userRoles = Set.of(roleRepository.getReferenceById(PredefinedRole.USER_ROLE));
        final var user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(userRoles);
        final var createdUser = userRepository.save(user);

        final var userProfileRequest = userProfileMapper.toUserProfileRequest(request);
        userProfileRequest.setUserId(user.getId());
        var response = profileClient.createUserProfile(userProfileRequest);
        log.info(response.toString());

        return userMapper.toUserResponse(createdUser);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        final var user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        userMapper.userUpdateRequestToUser(request, user);
        final var roles = roleRepository.findAllById(request.getRoles());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse getMyInfo() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var username = authentication.getName();
        return userMapper.toUserResponse(
                userRepository.findUserByUsername(username).orElseThrow(() -> new ServiceException(USER_NOT_FOUND)));
    }
}
