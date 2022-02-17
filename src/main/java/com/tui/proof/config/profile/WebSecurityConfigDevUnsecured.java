package com.tui.proof.config.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Profile("dev-unsecured")
@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class WebSecurityConfigDevUnsecured {

    @Bean
    protected SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.csrf().disable()
                .formLogin().disable()
                .authorizeExchange().anyExchange().permitAll()
                .and().build();
    }

}
