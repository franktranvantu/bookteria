package com.franktranvantu.bookteria.profile.service;

import com.franktranvantu.bookteria.profile.dto.request.UserProfileRequest;
import com.franktranvantu.bookteria.profile.dto.response.UserProfileResponse;
import com.franktranvantu.bookteria.profile.mapper.UserProfileMapper;
import com.franktranvantu.bookteria.profile.repository.UserProfileRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createUserProfile(UserProfileRequest request) {
        final var userProfile = userProfileMapper.toUserProfile(request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public List<UserProfileResponse> getUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse getUserProfile(String profileId) {
        return userProfileRepository
                .findById(profileId)
                .map(userProfileMapper::toUserProfileResponse)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
    }

    public UserProfileResponse updateUserProfile(String profileId, UserProfileRequest request) {
        final var user = userProfileRepository
                .findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        userProfileMapper.userProfileRequestToUserProfile(request, user);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(user));
    }

    public void deleteUserProfile(String userId) {
        userProfileRepository.deleteById(userId);
    }
}
