/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.service;

import com.example.middleware.exception.ApiException;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import com.example.middleware.providers.ShippingProvider;
import com.example.middleware.providers.ShippingProviderFactory;
import com.example.middleware.utils.AppUtils;
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
    private final AppUtils appUtils;

    @Override
    @Cacheable(value = "shippingRates")
    public List<ShippingRateData> getShippingRateData(ShippingRateRequest request) {
        primaryValidation(request);
        return request.providers().stream()
                .map(provider -> CompletableFuture.supplyAsync(() -> {
                    log.info("{} thread started", provider);
                    ShippingProvider shippingProvider = shippingProviderFactory.getShippingProvider(provider);
                    return shippingProvider.getShippingRateData(request);
                }))
                .map(future -> future.thenApply(result -> {
                    // Log information for each thread and when a thread finishes executing
                    log.info("{} thread finished", result.provider());
                    return result;
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private void primaryValidation(ShippingRateRequest request) {
        if (appUtils.getCountryCodeByName(request.origin_country()).isBlank()) {
            throw new ApiException("invalid origin_country");
        }
        if (appUtils.getCountryCodeByName(request.destination_country()).isBlank()) {
            throw new ApiException("invalid destination_country");
        }
        // add rules here
    }
}
