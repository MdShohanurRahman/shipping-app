/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.model;

import com.example.middleware.enums.CountryCode;
import com.example.middleware.enums.GoodsType;
import com.example.middleware.enums.Provider;
import com.example.middleware.exception.ApiException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.Set;

public record ShippingRateRequest(
        @Schema(defaultValue = "MY")
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
                example = "ID",
                description = "accept alfa-2 code"
        )
        String destination_country,
        String destination_state,
        String destination_postCode,

        @Schema(
                defaultValue = "PARCEL",
                allowableValues = {"PARCEL", "DOCUMENT"}
        )
        String goods_type,

        @Schema(
                defaultValue = "false",
                allowableValues = {"true", "false"}
        )
        boolean internationalShipping,

        @Schema(
                defaultValue = "false",
                allowableValues = {"true", "false"}
        )
        boolean expressDelivery,

        @DecimalMin("0.0")
        @Schema(
                defaultValue = "1"
        )
        Double length,

        @DecimalMin("0.0")
        @Schema(
                defaultValue = "1"
        )
        Double width,

        @DecimalMin("0.0")
        @Schema(
                defaultValue = "1"
        )
        Double height,

        @DecimalMin("0.1")
        @NotNull
        @Schema(
                defaultValue = "1"
        )
        Double weight,

        String insurance_item_value,

        @NotEmpty.List({
                @NotEmpty(message = "providers is required")
        })
        @Schema(
                allowableValues = {"CITY_LINK_EXPRESS", "JT_EXPRESS"}
        )
        Set<String> providers
) {
    public void validate() {
        providers().forEach(provider -> {
            if (!Provider.isValid(provider)) {
                throw new ApiException("invalid provider " + provider);
            }
        });
        if (!Objects.equals(origin_country(), "MY")) {
            throw new ApiException("origin country should be Malaysia");
        }
        if (CountryCode.getByCode(destination_country()) == null) {
            throw new ApiException("invalid destination_country");
        }
        if (!GoodsType.isValid(goods_type())) {
            throw new ApiException("invalid goods_type");
        }
    }
}

