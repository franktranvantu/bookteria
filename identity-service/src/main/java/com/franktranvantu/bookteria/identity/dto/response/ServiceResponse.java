package com.franktranvantu.bookteria.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.franktranvantu.bookteria.identity.exception.ServiceStatusCode;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> {
    int code;
    String message;
    T result;

    public static <T> ServiceResponse<T> ok(T result) {
        return ServiceResponse.<T>builder()
                .code(ServiceStatusCode.SUCCESS.getCode())
                .result(result)
                .build();
    }

    public static ServiceResponse ok() {
        return ServiceResponse.<Void>builder()
                .code(ServiceStatusCode.SUCCESS.getCode())
                .build();
    }
}
