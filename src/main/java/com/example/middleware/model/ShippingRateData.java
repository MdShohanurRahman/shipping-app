/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.model;

import com.example.middleware.enums.Provider;

import java.util.Map;

public record ShippingRateData(Provider provider, String rate, String message) {}

