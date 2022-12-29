package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom pre filter

        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var response = exchange.getResponse();

            log.info("Custom pre filter: request id -> {}", request.getId());

            // Custom post filter
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() ->
                            log.info("Custom post filter: response id -> {}", response.getStatusCode())));
        };
    }

    public static class Config{
        // Put the configuration props
    }

}
