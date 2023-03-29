package com.example.middleware.service;

import com.example.middleware.enums.Provider;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import com.example.middleware.providers.ShippingProvider;
import com.example.middleware.providers.ShippingProviderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class ShippingRateServiceImplTest {

    private ShippingRateRequest request;
    private final ShippingRateRequest request1 = new ShippingRateRequest(
            "MY",
            "Johor",
            "80100",
            "MY",
            "Johor",
            "80200",
            "PARCEL",
            false,
            1D,
            1D,
            1D,
            1D,
            0D,
            Set.of("CITY_LINK_EXPRESS", "JT_EXPRESS"));

    private final ShippingRateRequest request2 = new ShippingRateRequest(
            "MY",
            "Johor",
            "80100",
            "IDN",
            "Johor",
            "80200",
            "PARCEL",
            false,
            1D,
            1D,
            1D,
            1D,
            0D,
            Set.of("CITY_LINK_EXPRESS", "JT_EXPRESS"));



    @BeforeEach
    void setUp() {
       request = this.request2;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetShippingRateData() {
        // given
        List<ShippingRateData> shippingRateDataList;
        ShippingProviderFactory providerFactory = new ShippingProviderFactory();

        // when
        shippingRateDataList = request.providers().stream()
                .map(provider -> CompletableFuture.supplyAsync(() -> {
                    ShippingProvider shippingProvider = providerFactory.getShippingProvider(Provider.valueOf(provider));
                    return shippingProvider.getShippingRateData(request);
                }))
                .map(future -> future.thenApply(result -> result))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // then
        Assertions.assertEquals(2, shippingRateDataList.size());
        Assertions.assertTrue(
                shippingRateDataList.stream()
                        .allMatch(shippingRateData -> shippingRateData.getProvider().equals(Provider.CITY_LINK_EXPRESS)
                                || shippingRateData.getProvider().equals(Provider.JT_EXPRESS))
        );
    }

}