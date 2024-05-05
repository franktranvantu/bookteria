package com.franktranvantu.bookteria.profile.controller;

import com.franktranvantu.bookteria.profile.dto.request.UserProfileRequest;
import com.franktranvantu.bookteria.profile.dto.response.UserProfileResponse;
import com.franktranvantu.bookteria.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal-users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PutMapping("/{profileId}")
    public UserProfileResponse updateUser(@PathVariable String profileId, @RequestBody UserProfileRequest request) {
        return userProfileService.updateUserProfile(profileId, request);
    }

    @DeleteMapping("/{profileId}")
    public void deleteUser(@PathVariable String profileId) {
        userProfileService.deleteUserProfile(profileId);
    }
}
