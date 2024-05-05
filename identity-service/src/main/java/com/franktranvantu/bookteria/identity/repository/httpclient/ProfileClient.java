package com.franktranvantu.bookteria.identity.repository.httpclient;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.franktranvantu.bookteria.identity.dto.request.UserProfileRequest;
import com.franktranvantu.bookteria.identity.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @PostMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
    UserProfileResponse createUserProfile(@RequestBody UserProfileRequest request);
}
