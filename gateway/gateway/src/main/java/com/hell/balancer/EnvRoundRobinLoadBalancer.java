package com.hell.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EnvRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(EnvRoundRobinLoadBalancer.class);
    final AtomicInteger position;
    final String serviceId;
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public EnvRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider, serviceId, (new Random()).nextInt(1000));
    }

    public EnvRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        RequestDataContext requestDataContext = (RequestDataContext) request.getContext();
        HttpHeaders headers = requestDataContext.getClientRequest().getHeaders();

        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) -> {
            return this.processInstanceResponse(supplier, serviceInstances, headers);
        });
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances, HttpHeaders headers) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances, headers);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }

        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + this.serviceId);
            }

            return new EmptyResponse();
        } else {
            List<ServiceInstance> serviceInstance = instances.stream().filter(instance -> {
                String envKey = headers.getFirst("X-Request-EnvKey");
                if (envKey != null || !envKey.equals("")) {
                    return envKey.equals(instance.getMetadata().get("X-Request-EnvKey"));
                } else {
                    log.warn("No X-Request-EnvKey for service: " + this.serviceId);
                    return false;
                }
            }).collect(Collectors.toList());
            if(serviceInstance.isEmpty()) {
                if (log.isWarnEnabled()) {
                    log.warn("No servers available for service: " + this.serviceId);
                }
                return new EmptyResponse();
            }
            int pos = Math.abs(this.position.incrementAndGet());
            ServiceInstance instance = serviceInstance.get(pos % serviceInstance.size());
            return new DefaultResponse(instance);
        }
    }
}
