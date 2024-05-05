package com.franktranvantu.bookteria.profile.mapper;

import com.franktranvantu.bookteria.profile.dto.request.UserProfileRequest;
import com.franktranvantu.bookteria.profile.dto.response.UserProfileResponse;
import com.franktranvantu.bookteria.profile.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest dto);

    UserProfileResponse toUserProfileResponse(UserProfile entity);

    void userProfileRequestToUserProfile(UserProfileRequest dto, @MappingTarget UserProfile entity);
}
