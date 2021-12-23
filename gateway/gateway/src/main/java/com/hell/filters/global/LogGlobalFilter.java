package com.hell.filters.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class LogGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(LogGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();
        log.info(exchange.getRequest().getId() +
                " request uri:" + exchange.getRequest().getPath()
                + " start time:" + start);
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long end = System.currentTimeMillis();
            log.info(exchange.getRequest().getId() +
                    " response uri:" + exchange.getRequest().getPath()
                    + " end time:" + end);
        }));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
