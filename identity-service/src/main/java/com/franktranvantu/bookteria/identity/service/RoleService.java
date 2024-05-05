package com.franktranvantu.bookteria.identity.service;

import static com.franktranvantu.bookteria.identity.exception.ServiceStatusCode.PERMISSION_EXISTED;
import static com.franktranvantu.bookteria.identity.exception.ServiceStatusCode.ROLE_NOT_FOUND;

import com.franktranvantu.bookteria.identity.dto.request.RoleRequest;
import com.franktranvantu.bookteria.identity.dto.response.RoleResponse;
import com.franktranvantu.bookteria.identity.exception.ServiceException;
import com.franktranvantu.bookteria.identity.mapper.RoleMapper;
import com.franktranvantu.bookteria.identity.repository.PermissionRepository;
import com.franktranvantu.bookteria.identity.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.findById(request.getName()).isPresent()) {
            throw new ServiceException(PERMISSION_EXISTED);
        }
        final var role = roleMapper.toRole(request);
        final var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public RoleResponse getRole(String roleName) {
        return roleRepository
                .findById(roleName)
                .map(roleMapper::toRoleResponse)
                .orElseThrow(() -> new ServiceException(ROLE_NOT_FOUND));
    }

    public RoleResponse updateRole(String roleName, RoleRequest request) {
        final var role = roleRepository.findById(roleName).orElseThrow(() -> new ServiceException(ROLE_NOT_FOUND));
        roleMapper.roleUpdateRequestToRole(request, role);
        final var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public void deleteRole(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
