/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class AppUtils {

    static Map<String, String> countryCodes = new HashMap<>();

    /*
    * @param countryName ex: "Malaysia", "Bangladesh"
    * */
    public static String getCountryCodeByName(String countryName) {
        // if already have value then return from map
        if (countryCodes.containsKey(countryName)) {
            return countryCodes.get(countryName);
        }
        String[] isoCountries = Locale.getISOCountries();
        for (String countryCode : isoCountries) {
            Locale obj = new Locale("", countryCode);
            if (obj.getDisplayCountry().equals(countryName)) {
                countryCodes.put(countryName, obj.getCountry());
                return obj.getCountry();
            }
        }
        return "";
    }
}
