package com.franktranvantu.bookteria.identity.mapper;

import com.franktranvantu.bookteria.identity.dto.request.UserCreationRequest;
import com.franktranvantu.bookteria.identity.dto.request.UserUpdateRequest;
import com.franktranvantu.bookteria.identity.dto.response.RoleResponse;
import com.franktranvantu.bookteria.identity.dto.response.UserResponse;
import com.franktranvantu.bookteria.identity.entity.Role;
import com.franktranvantu.bookteria.identity.entity.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest dto);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserUpdateRequest dto);

    @Mapping(target = "roles", qualifiedByName = "mapRoles")
    UserResponse toUserResponse(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void userUpdateRequestToUser(UserUpdateRequest dto, @MappingTarget User entity);

    @Named("mapRoles")
    default Set<RoleResponse> mapRoles(Set<Role> roles) {
        RoleMapper roleMapper = new RoleMapperImpl();
        return roles.stream()
                .map(role -> RoleResponse.builder()
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(roleMapper.mapPermissions(role.getPermissions()))
                        .build())
                .collect(Collectors.toSet());
    }
}
