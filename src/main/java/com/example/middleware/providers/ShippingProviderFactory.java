/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.Provider;
import org.springframework.stereotype.Component;

@Component
public record ShippingProviderFactory() {

    public ShippingProvider getShippingProvider(Provider provider) {
        return switch (provider) {
            case CITY_LINK_EXPRESS -> new CityLinkExpressProvider();
            case JT_EXPRESS -> new JTExpressProvider();
        };
    }
}
