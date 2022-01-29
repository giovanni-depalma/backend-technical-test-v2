package com.tui.proof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@OpenAPIDefinition(info = @Info(title = "TUI Pilotes API", version = "1.0"))
@SecurityScheme(name = "secure-api", type = SecuritySchemeType.OPENIDCONNECT, in = SecuritySchemeIn.HEADER)
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}
