/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;

public interface ShippingProvider {

    ShippingRateData getShippingRateData(ShippingRateRequest request);

}
