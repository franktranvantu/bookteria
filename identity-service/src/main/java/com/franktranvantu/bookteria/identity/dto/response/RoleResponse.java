package com.franktranvantu.bookteria.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RoleResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
