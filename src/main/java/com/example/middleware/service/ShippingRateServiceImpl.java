/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.service;

import com.example.middleware.enums.Provider;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import com.example.middleware.providers.ShippingProvider;
import com.example.middleware.providers.ShippingProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingRateServiceImpl implements ShippingRateService {

    private final ShippingProviderFactory shippingProviderFactory;

    @Override
    @Cacheable(value = "shippingRates")
    public List<ShippingRateData> getShippingRateData(ShippingRateRequest request) {
        request.validate();
        return request.providers().stream()
                .map(provider -> CompletableFuture.supplyAsync(() -> {
                    log.info("{} executing started", provider);
                    ShippingProvider shippingProvider = shippingProviderFactory.getShippingProvider(Provider.valueOf(provider));
                    return shippingProvider.getShippingRateData(request);
                }))
                .map(future -> future.thenApply(result -> {
                    log.info("{} executing finished", result.getProvider());
                    return result;
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
