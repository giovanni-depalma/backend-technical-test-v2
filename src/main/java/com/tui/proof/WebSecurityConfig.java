package com.tui.proof;

import com.nimbusds.openid.connect.sdk.claims.ClaimsSet;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.Security;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                            try {
                                authorize
                                        .antMatchers("/public/orders").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                )
                .csrf()
                .disable()
                .oauth2Login(withDefaults());
    }


    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
        return converter;
    }

    public class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            final Map<String, HashMap<String, ?>> resourcesAccess = (Map<String, HashMap<String, ?>>) jwt.getClaims().get("resource_access");
            final Map<String, List<String>> clientResources = (Map<String, List<String>>)resourcesAccess.get("tui-gateway");
            return  clientResources.get("roles").stream()
                    .map(roleName -> "ROLE_" + roleName.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }

}