/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.controller;

import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import com.example.middleware.model.ShippingRateResponse;
import com.example.middleware.service.ShippingRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipping-rates")
public class ShippingRateController {

    private final ShippingRateService shippingRateService;

    @PostMapping()
    public ResponseEntity<ShippingRateResponse> shippingRates(@Valid @RequestBody ShippingRateRequest request) {
        List<ShippingRateData> shippingRates = shippingRateService.getShippingRateData(request);
        return ResponseEntity.ok(new ShippingRateResponse(shippingRates));
    }

    @GetMapping("/clear-cache")
    @CacheEvict(value = "shippingRates", allEntries = true)
    public void clearCache() {}
}
