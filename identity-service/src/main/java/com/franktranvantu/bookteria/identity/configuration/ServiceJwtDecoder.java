package com.franktranvantu.bookteria.identity.configuration;

import static com.franktranvantu.bookteria.identity.exception.ServiceStatusCode.UNAUTHENTICATED;

import com.franktranvantu.bookteria.identity.dto.request.IntrospectRequest;
import com.franktranvantu.bookteria.identity.exception.ServiceException;
import com.franktranvantu.bookteria.identity.service.AuthenticationService;
import com.nimbusds.jose.JWSAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    @NonFinal
    String signerKey;

    AuthenticationService authenticationService;

    @Override
    public Jwt decode(String token) throws JwtException {
        IntrospectRequest introspectRequest =
                IntrospectRequest.builder().token(token).build();
        final var isValid = authenticationService.introspect(introspectRequest).isValid();
        if (!isValid) {
            throw new ServiceException(UNAUTHENTICATED);
        }
        final var secretKey = new SecretKeySpec(signerKey.getBytes(), JWSAlgorithm.HS512.getName());
        final var nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return nimbusJwtDecoder.decode(token);
    }
}
