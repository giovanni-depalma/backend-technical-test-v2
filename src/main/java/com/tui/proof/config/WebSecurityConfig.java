package com.tui.proof.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.*;
import java.util.stream.Collectors;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityParameters securityParameters;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .antMatchers(securityParameters.getWhiteList()).permitAll()
                                .anyRequest().hasRole("ADMIN")
                                .and()
                                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
                    } catch (Exception e) {
                        log.error("Error WebSecurityConfiguration", e);
                    }
                })
                .csrf()
                .disable();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
        return converter;
    }

    public static class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public List<GrantedAuthority> convert(Jwt jwt) {
            try{
                final Map<String, Map<String, ?>> resourcesAccess = (Map<String, Map<String, ?>>) jwt.getClaims()
                        .get("resource_access");
                final Map<String, List<String>> clientResources = (Map<String, List<String>>) resourcesAccess
                        .get("tui-gateway");
                log.trace("clientResources: {}", clientResources);
                return clientResources.get("roles").stream()
                        .map(roleName -> "ROLE_" + roleName.toUpperCase())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
            catch(Exception e){
                log.error("convert jwt error",e);
                throw e;
            }

        }
    }


}