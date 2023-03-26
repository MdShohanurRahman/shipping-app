/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.utils;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppUtils {

    /*
     * @param countryName ex: "Malaysia", "Bangladesh"
     * return ML, BD
     * */
    @Cacheable("countryCodes")
    public String getCountryCodeByName(String countryName) {
        String[] isoCountries = Locale.getISOCountries();
        for (String countryCode : isoCountries) {
            Locale obj = new Locale("", countryCode);
            if (obj.getDisplayCountry().equals(countryName)) {
                return obj.getCountry();
            }
        }
        return "";
    }
}
