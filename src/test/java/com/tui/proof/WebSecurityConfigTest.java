package com.tui.proof;

import com.tui.proof.config.WebSecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebSecurityConfigTest {

    @Test
    public void shouldGetGrantedAuthorities(){
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
        List<GrantedAuthority> actual = converter.convert(jwt);
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("ROLE_ADMIN", actual.get(0).toString());
        assertEquals("ROLE_CUSTOMER", actual.get(1).toString());
        assertEquals("ROLE_OTHER", actual.get(2).toString());
    }

    @Test
    public void shouldThrowException(){
        WebSecurityConfig.RealmRoleConverter converter = new WebSecurityConfig.RealmRoleConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("scope", "message:read")
                .build();
        Assertions.assertThrows(Exception.class, () -> converter.convert(jwt));
    }
}
