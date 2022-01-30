package com.tui.proof;

import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .antMatchers("/purchaserOrders/**").permitAll()
                                .antMatchers("/swagger-ui/**").permitAll()
                                .antMatchers("/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private static class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            try{
                final Map<String, HashMap<String, ?>> resourcesAccess = (Map<String, HashMap<String, ?>>) jwt.getClaims()
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