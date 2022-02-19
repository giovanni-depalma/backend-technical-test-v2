package com.tui.proof;

import com.tui.proof.config.WebSecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSecurityConfigTest {

    @Test
    public void shouldGetGrantedAuthorities() {
        WebSecurityConfig.RealmRoleConverter converter = new WebSecurityConfig.RealmRoleConverter();
        final Map<String, Map<String, ?>> resourceAccess = new HashMap<>();
        final Map<String, Object> clientResources = new HashMap<>();
        clientResources.put("roles", List.of("admin", "customer", "other"));
        resourceAccess.put("tui-gateway", clientResources);
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("scope", "message:read")
                .claim("resource_access", resourceAccess)
                .build();
        Flux<GrantedAuthority> actual = converter.convert(jwt);
        Assertions.assertNotNull(actual);
        StepVerifier.create(actual.map(Object::toString))
                .expectNext("ROLE_ADMIN")
                .expectNext("ROLE_CUSTOMER")
                .expectNext("ROLE_OTHER")
                .verifyComplete();
    }

    @Test
    public void shouldThrowException() {
        WebSecurityConfig.RealmRoleConverter converter = new WebSecurityConfig.RealmRoleConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("scope", "message:read")
                .build();
        Flux<GrantedAuthority> actual = converter.convert(jwt);
        Assertions.assertNotNull(actual);
        StepVerifier.create(actual).expectError().verify();
    }
}
