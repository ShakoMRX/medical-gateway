package io.optimogroup.xracoon.asclepius.medicalgateway.config;

import io.optimogroup.xracoon.asclepius.medicalgateway.handlers.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Created by shako on 05/08/2021.
 */
@Configuration
public class RouterConfiguration {
    private final LoginHandler loginHandler;

    public RouterConfiguration(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> jopaRouter() {
        return RouterFunctions
                .route(GET("/step1")
                        .and(accept(MediaType.APPLICATION_JSON)), loginHandler::step1).
                andRoute(GET("/secured/step2")
                        .and(accept(MediaType.APPLICATION_JSON)), loginHandler::step2);
    }
}
