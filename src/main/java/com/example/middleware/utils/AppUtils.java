/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppUtils {

    private static final Map<String, String> countryCodes = new ConcurrentHashMap<>();

    List<String> jitExpressDestinationCountryList = List.of(
            "BWN", "HKG", "SIN", "VNM", "CHN", "THA", "IDN", "OHL"
    );

    /*
     * @param countryName ex: ML, BD
     * return  Malaysia, Bangladesh
     * */
    public static String getCountryCode(String countryName) {
        if (countryCodes.containsKey(countryName)) {
            return countryCodes.get(countryName);
        }
        String[] isoCountries = Locale.getISOCountries();
        for (String countryCode : isoCountries) {
            Locale obj = new Locale("", countryCode);
            countryCodes.put(obj.getCountry(), obj.getDisplayCountry());
        }
        return countryCodes.get(countryName);
    }
}
