/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.model;

import com.example.middleware.enums.Provider;
import com.example.middleware.exception.ApiException;
import com.example.middleware.utils.AppUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

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
        String length,
        String width,
        String height,
        String selected_type,
        String parcel_weight,
        String document_weight,
        @NotEmpty
        List<Provider> providers
) {
    public void primaryValidation() {
        if (AppUtils.getCountryCodeByName(origin_country()).isBlank()) {
            throw new ApiException("invalid origin_country");
        }
        if (AppUtils.getCountryCodeByName(destination_country()).isBlank()) {
            throw new ApiException("invalid destination_country");
        }
        // add other validation rules as needed
    }
}