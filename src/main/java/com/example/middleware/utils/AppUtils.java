/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.utils;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppUtils {

    static Map<String, String> countryCodes = new ConcurrentHashMap<>();

    /*
    * @param countryName ex: "Malaysia", "Bangladesh"
    * */
    public static String getCountryCodeByName(String countryName) {
        // if exists then return from countryCodes map
        if (countryCodes.containsKey(countryName)) {
            return countryCodes.get(countryName);
        }
        String[] isoCountries = Locale.getISOCountries();
        for (String countryCode : isoCountries) {
            Locale obj = new Locale("", countryCode);
            if (obj.getDisplayCountry().equals(countryName)) {
                countryCodes.put(countryName, obj.getCountry());
            }
        }
        return countryCodes.getOrDefault(countryName, "");
    }
}
