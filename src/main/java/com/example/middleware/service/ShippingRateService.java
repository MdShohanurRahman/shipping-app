/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.service;

import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;

import java.util.List;

public interface ShippingRateService {

    List<ShippingRateData> getShippingRateData(ShippingRateRequest request);
}
