/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.model;

import com.example.middleware.enums.GoodsType;
import com.example.middleware.enums.Provider;
import com.example.middleware.exception.ApiException;
import com.example.middleware.utils.AppUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;
import java.util.Set;

public record ShippingRateRequest(
        @Schema(defaultValue = "Malaysia")
        @NotBlank
        String origin_country,

        @NotBlank
        @Schema(
                example = "Johor"
        )
        String origin_state,

        @NotBlank
        @Schema(
                example = "80100"
        )
        String origin_post_code,

        @NotBlank
        @Schema(
                example = "BD"
        )
        String destination_country,

        @Schema(
                defaultValue = "PARCEL",
                allowableValues = {"PARCEL", "DOCUMENT"}
        )
        String goods_type,

        Double length,
        Double width,
        Double height,
        Double parcel_weight,
        Double document_weight,

        @NotEmpty
        @Schema(
                allowableValues = {"CITY_LINK", "JT_EXPRESS"}
        )
        Set<String> providers
) {
    public void validate() {
        providers().forEach(provider -> {
            if (!Provider.isValid(provider)) {
                throw new ApiException("invalid provider " + provider);
            }
        });
        if (!Objects.equals(origin_country(), "Malaysia")) {
            throw new ApiException("origin country should be Malaysia");
        }
        if (AppUtils.getCountryCode(destination_country()) == null) {
            throw new ApiException("invalid destination_country");
        }
        if (!GoodsType.isValid(goods_type())) {
            throw new ApiException("invalid goods_type");
        }
    }
}

