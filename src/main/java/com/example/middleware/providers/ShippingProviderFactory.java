/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.Provider;
import com.example.middleware.utils.AppUtils;
import org.springframework.stereotype.Component;

@Component
public record ShippingProviderFactory(AppUtils appUtils) {

    public ShippingProvider getShippingProvider(Provider provider) {
        return switch (provider) {
            case CITY_LINK -> new CityLinkProvider(appUtils);
            case JT_EXPRESS -> new JTExpressProvider(appUtils);
        };
    }
}
