package com.franktranvantu.bookteria.identity.configuration;

import static com.franktranvantu.bookteria.identity.constant.PredefinedRole.ADMIN_ROLE;
import static com.franktranvantu.bookteria.identity.constant.PredefinedRole.USER_ROLE;

import com.franktranvantu.bookteria.identity.dto.request.UserProfileRequest;
import com.franktranvantu.bookteria.identity.entity.Permission;
import com.franktranvantu.bookteria.identity.entity.Role;
import com.franktranvantu.bookteria.identity.entity.User;
import com.franktranvantu.bookteria.identity.repository.PermissionRepository;
import com.franktranvantu.bookteria.identity.repository.RoleRepository;
import com.franktranvantu.bookteria.identity.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.franktranvantu.bookteria.identity.repository.httpclient.ProfileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class InitialConfiguration {
    @Bean
    public ApplicationRunner runner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            ProfileClient profileClient) {
        return args -> {
            if (userRepository.findUserByUsername("admin").isEmpty()) {
                final var createPost = Permission.builder()
                        .name("CREATE_POST")
                        .description("The owner of this permission can create the posts")
                        .build();
                final var approvePost = Permission.builder()
                        .name("APPROVE_POST")
                        .description("The owner of this permission can approve the posts")
                        .build();
                final var rejectPost = Permission.builder()
                        .name("REJECT_POST")
                        .description("The owner of this permission can reject the posts")
                        .build();
                permissionRepository.saveAll(List.of(createPost, approvePost, rejectPost));

                final var userRole = Role.builder()
                        .name(USER_ROLE)
                        .description("The User role")
                        .permissions(Set.of(createPost))
                        .build();
                final var adminRole = Role.builder()
                        .name(ADMIN_ROLE)
                        .description("The Admin role")
                        .permissions(Set.of(createPost, approvePost, rejectPost))
                        .build();
                roleRepository.saveAll(List.of(userRole, adminRole));

                final var user1 = User.builder()
                        .id("5fef4cd3-9544-4d22-a032-28d660c09120")
                        .username("user1")
                        .password(passwordEncoder.encode("pass"))
                        .roles(Set.of(userRole))
                        .build();
                final var user2 = User.builder()
                        .id("1f15bcef-09cb-477d-b686-25c333d55f56")
                        .username("user2")
                        .password(passwordEncoder.encode("pass"))
                        .roles(Set.of(userRole))
                        .build();
                final var admin = User.builder()
                        .id("d2b4466a-4570-4a5f-b7be-9c871c29bb28")
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(Set.of(adminRole))
                        .build();
                userRepository.saveAll(List.of(user1, user2, admin));

                profileClient.createUserProfile(UserProfileRequest
                        .builder()
                        .userId(user1.getId())
                        .firstName("User 1")
                        .dateOfBirth(LocalDate.now())
                        .city("Ho Chi Minh")
                        .build()
                );
                profileClient.createUserProfile(UserProfileRequest
                        .builder()
                        .userId(user2.getId())
                        .firstName("User 2")
                        .dateOfBirth(LocalDate.now())
                        .city("Ho Chi Minh")
                        .build()
                );
                profileClient.createUserProfile(UserProfileRequest
                        .builder()
                        .userId(admin.getId())
                        .firstName("Admin")
                        .dateOfBirth(LocalDate.now())
                        .city("Ho Chi Minh")
                        .build()
                );

                log.info(""
                        + "\nThe application was automatically init various example data for testing purpose as below:"
                        + "\n\tPermissions: CREATE_POST; APPROVE_POST; REJECT_POST"
                        + "\n\tRoles: USER had permission CREATE_POST; ADMIN had permissions CREATE_POST, APPROVE_POST, REJECT_POST"
                        + "\n\tUsers: user1/pass had role USER; user2/pass had role USER, admin/admin had role ADMIN");
            }
        };
    }
}
