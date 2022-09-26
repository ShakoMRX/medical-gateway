package io.optimogroup.xracoon.asclepius.medicalgateway.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
public class LoginHandler {
    public Mono<ServerResponse> step1(ServerRequest request) {
        return request.session().map(webSession -> {
            String traki = webSession.getAttributeOrDefault("redirect", null);
            // from referer header we will get redirect uri
            webSession.getAttributes().putIfAbsent("redirect", "wwww.google.com");
            return "traki";
        }).flatMap(o -> ServerResponse.temporaryRedirect(URI.create("/secured/step2")).build());
    }

    public Mono<ServerResponse> step2(ServerRequest request) {
        return request.session().map(webSession -> {
            String traki = webSession.getAttributeOrDefault("redirect", "NO_OP");
            return traki;
        }).filter(s -> !s.equals("NO_OP")).flatMap(s -> {
            return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue("successfully redirected in jopa, we must be in " + s));
        }).switchIfEmpty(ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("redirect uri not found")));
    }
}
