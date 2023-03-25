/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.Provider;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import org.springframework.stereotype.Component;

@Component
public class JTExpressProvider implements ShippingProvider {

    @Override
    public ShippingRateData getShippingRateData(ShippingRateRequest request) {
        return new ShippingRateData(
                Provider.JT_EXPRESS,
                "0",
                "this provider has no public api"
        );
    }
}
