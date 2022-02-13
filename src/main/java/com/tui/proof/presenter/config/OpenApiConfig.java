package com.tui.proof.presenter.config;

import com.tui.proof.domain.entities.base.Money;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "TUI Pilotes API", version = "1.0"))
@SecurityScheme(name = "secure-api", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(
    password = @OAuthFlow(authorizationUrl = "${is.external-realm-url}/protocol/openid-connect/auths",
    tokenUrl = "${is.external-realm-url}/protocol/openid-connect/token")
    ), in = SecuritySchemeIn.HEADER)
@SecurityScheme(name = "secure-api2", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT" , in = SecuritySchemeIn.HEADER)

public class OpenApiConfig {

    static {
        SpringDocUtils.getConfig()
                .replaceWithSchema(Money.class, new StringSchema().example("13.33 €"));
    }
   
}
