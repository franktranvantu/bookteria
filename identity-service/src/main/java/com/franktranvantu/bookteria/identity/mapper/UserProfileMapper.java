package com.franktranvantu.bookteria.identity.mapper;

import com.franktranvantu.bookteria.identity.dto.request.UserCreationRequest;
import com.franktranvantu.bookteria.identity.dto.request.UserProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    @Mapping(source = "dob", target = "dateOfBirth")
    UserProfileRequest toUserProfileRequest(UserCreationRequest dto);
}
