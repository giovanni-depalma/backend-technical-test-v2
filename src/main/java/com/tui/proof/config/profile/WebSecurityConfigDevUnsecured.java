package com.tui.proof.config.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("dev-unsecured")
@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class WebSecurityConfigDevUnsecured extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable();
    }

    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers("/**");
    }
}
