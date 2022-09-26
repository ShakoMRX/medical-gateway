package io.optimogroup.xracoon.asclepius.medicalgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * Created by shako on 08/24/2022.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {

        ServerWebExchangeMatcher xhrMatcher = (exchange) -> {
            if (exchange.getRequest().getHeaders().getOrEmpty("X-Requested-With").contains("XMLHttpRequest")) {
                return ServerWebExchangeMatcher.MatchResult.match();
            }
            return ServerWebExchangeMatcher.MatchResult.notMatch();
        };

        DelegatingServerAuthenticationEntryPoint.DelegateEntry entryPoints =
                new DelegatingServerAuthenticationEntryPoint.DelegateEntry(xhrMatcher, new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));

        DelegatingServerAuthenticationEntryPoint nonAjaxLoginEntryPoint = new DelegatingServerAuthenticationEntryPoint(entryPoints);



        nonAjaxLoginEntryPoint.setDefaultEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/keycloak"));


        SecurityWebFilterChain chain = http.csrf().disable()
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec.
                                pathMatchers("/api/**","/login").permitAll().anyExchange().authenticated()
                )
                .oauth2Login()
                .and()
                .exceptionHandling().authenticationEntryPoint(nonAjaxLoginEntryPoint)
                .and()
                .build();

        return chain;
    }
}
