package com.hell.filters.global;

import com.hell.balancer.EnvRoundRobinLoadBalancer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@Component
public class EnvRoundRobinLoadBalancerFilter implements GlobalFilter, Ordered {
    private static final Log log = LogFactory.getLog(ReactiveLoadBalancerClientFilter.class);
    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10151;
    private final LoadBalancerClientFactory clientFactory;
    private final GatewayLoadBalancerProperties properties;
    private final LoadBalancerProperties loadBalancerProperties;

    public EnvRoundRobinLoadBalancerFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties, LoadBalancerProperties loadBalancerProperties) {
        this.clientFactory = clientFactory;
        this.properties = properties;
        this.loadBalancerProperties = loadBalancerProperties;
    }

    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = (URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = (String) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url != null && ("envLb".equals(url.getScheme()) || "envLb".equals(schemePrefix))) {
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);
            if (log.isTraceEnabled()) {
                log.trace(EnvRoundRobinLoadBalancerFilter.class.getSimpleName() + " url before: " + url);
            }

            URI requestUri = (URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            String serviceId = requestUri.getHost();
            Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator.getSupportedLifecycleProcessors(this.clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class), RequestDataContext.class, ResponseData.class, ServiceInstance.class);
            DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest(new RequestDataContext(new RequestData(exchange.getRequest()), this.getHint(serviceId, this.loadBalancerProperties.getHint())));
            return this.choose(lbRequest, serviceId, supportedLifecycleProcessors).doOnNext((response) -> {
                if (!response.hasServer()) {
                    supportedLifecycleProcessors.forEach((lifecycle) -> {
                        lifecycle.onComplete(new CompletionContext(CompletionContext.Status.DISCARD, lbRequest, response));
                    });
                    throw NotFoundException.create(this.properties.isUse404(), "Unable to find instance for " + url.getHost());
                } else {
                    ServiceInstance retrievedInstance = (ServiceInstance) response.getServer();
                    URI uri = exchange.getRequest().getURI();
                    String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
                    if (schemePrefix != null) {
                        overrideScheme = url.getScheme();
                    }

                    DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(retrievedInstance, overrideScheme);
                    URI requestUrl = this.reconstructURI(serviceInstance, uri);
                    if (log.isTraceEnabled()) {
                        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                    }

                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);
                    supportedLifecycleProcessors.forEach((lifecycle) -> {
                        lifecycle.onStartRequest(lbRequest, response);
                    });
                }
            }).then(chain.filter(exchange)).doOnError((throwable) -> {
                supportedLifecycleProcessors.forEach((lifecycle) -> {
                    lifecycle.onComplete(new CompletionContext(CompletionContext.Status.FAILED, throwable, lbRequest, (Response) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR)));
                });
            }).doOnSuccess((aVoid) -> {
                supportedLifecycleProcessors.forEach((lifecycle) -> {
                    lifecycle.onComplete(new CompletionContext(CompletionContext.Status.SUCCESS, lbRequest, (Response) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR), new ResponseData(exchange.getResponse(), new RequestData(exchange.getRequest()))));
                });
            });
        } else {
            return chain.filter(exchange);
        }
    }

    protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    private Mono<Response<ServiceInstance>> choose(Request<RequestDataContext> lbRequest, String serviceId, Set<LoadBalancerLifecycle> supportedLifecycleProcessors) {
//        ReactorLoadBalancer<ServiceInstance> loadBalancer = (ReactorLoadBalancer) this.clientFactory.getInstance(serviceId, EnvRoundRobinLoadBalancer.class);
        EnvRoundRobinLoadBalancer loadBalancer = new EnvRoundRobinLoadBalancer(clientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
        if (loadBalancer == null) {
            throw new NotFoundException("No loadbalancer available for " + serviceId);
        } else {
            supportedLifecycleProcessors.forEach((lifecycle) -> {
                lifecycle.onStart(lbRequest);
            });
            return loadBalancer.choose(lbRequest);
        }
    }

    private String getHint(String serviceId, Map<String, String> hints) {
        String defaultHint = (String) hints.getOrDefault("default", "default");
        String hintPropertyValue = (String) hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }
}
