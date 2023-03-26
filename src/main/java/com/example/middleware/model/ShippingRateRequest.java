/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.model;

import com.example.middleware.enums.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record ShippingRateRequest(
        @NotBlank
        String origin_country,
        @NotBlank
        String origin_state,
        @NotBlank
        String origin_post_code,
        @NotBlank
        String destination_country,
        String destination_state,
        String destination_post_code,
        Double length,
        Double width,
        Double height,
        String selected_type,
        Double parcel_weight,
        Double document_weight,
        @NotEmpty
        Set<Provider> providers
) { }