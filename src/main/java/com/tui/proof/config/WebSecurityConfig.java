package com.tui.proof.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class WebSecurityConfig{

    private final WebSecurityConfigParameters securityParameters;

    @Bean
    protected SecurityWebFilterChain configure(ServerHttpSecurity  http) {
        return http.csrf().disable()
                .authorizeExchange()
                                .pathMatchers(securityParameters.getWhiteList()).permitAll()
                                .pathMatchers(HttpMethod.POST, "/orders").permitAll()
                                .pathMatchers(HttpMethod.PUT, "/orders/*").permitAll()
                                .anyExchange().hasRole("ADMIN")
                                .and()
                                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and().and().build();
    }

    private ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
        return converter;
    }

    public static class RealmRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {
        @Override
        public Flux<GrantedAuthority> convert(Jwt jwt) {
            try{
                final Map<String, Map<String, ?>> resourcesAccess = (Map<String, Map<String, ?>>) jwt.getClaims()
                        .get("resource_access");
                final Map<String, List<String>> clientResources = (Map<String, List<String>>) resourcesAccess
                        .get("tui-gateway");
                log.trace("clientResources: {}", clientResources);
                return Flux.fromStream(clientResources.get("roles").stream()
                        .map(roleName -> "ROLE_" + roleName.toUpperCase())
                        .map(SimpleGrantedAuthority::new)
                        );
            }
            catch(Exception e){
                return Flux.error(e);
            }

        }
    }


}