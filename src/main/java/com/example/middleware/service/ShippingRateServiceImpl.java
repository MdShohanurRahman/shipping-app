/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.service;

import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import com.example.middleware.providers.ShippingProvider;
import com.example.middleware.providers.ShippingProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<ShippingRateData> getShippingRateData(ShippingRateRequest request) {
        request.primaryValidation();
        return request.providers().stream()
                .map(provider -> CompletableFuture.supplyAsync(() -> {
                    ShippingProvider shippingProvider = shippingProviderFactory.getShippingProvider(provider);
                    return shippingProvider.getShippingRateData(request);
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
