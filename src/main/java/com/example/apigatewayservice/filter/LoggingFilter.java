package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(((exchange, chain) -> {
            var request = exchange.getRequest();
            var response = exchange.getResponse();

            log.info("Logging Filter baseMessage: {}" , config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Logging Pre Filter: request id -> {}" , request.getId());
            }

            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() ->{
                        if(config.isPostLogger()){
                            log.info("Logging Post Filter: response code -> {}" , response.getStatusCode());
                        }
                    }));
        }), Ordered.HIGHEST_PRECEDENCE);
    }

    @Data
    public static class Config {
        // Put the configuration props

        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}
