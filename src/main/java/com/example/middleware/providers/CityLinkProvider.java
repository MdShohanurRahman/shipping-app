/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.Provider;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import com.example.middleware.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CityLinkProvider implements ShippingProvider {

    private final AppUtils appUtils;

    @Override
    public ShippingRateData getShippingRateData(ShippingRateRequest request) {
        try {
            var response = getResponse(request);

            if (response.getStatusCode().value() != 200) {
                return new ShippingRateData(
                        Provider.CITY_LINK, "0", "invalid request"
                );
            }

            // Get the response body
            var responseBody = response.getBody();
            Map req = (Map) responseBody.get("req");
            Map data = (Map) req.get("data");

            return new ShippingRateData(
                    Provider.CITY_LINK, String.valueOf(data.get("rate")), (String) data.get("message")
            );
        }
        catch (Exception exception) {
            exception.printStackTrace();
            log.error("can't get data from CITY_LINK");
            return new ShippingRateData(
                    Provider.CITY_LINK, "0", exception.getMessage()
            );
        }
    }

    private ResponseEntity<Map> getResponse(ShippingRateRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // prepare formData
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("origin_country", appUtils.getCountryCodeByName(request.origin_country()));
        formData.add("origin_state", request.origin_state());
        formData.add("origin_postcode", request.origin_post_code());
        formData.add("destination_country", appUtils.getCountryCodeByName(request.destination_country()));
        formData.add("destination_state", request.destination_country());
        formData.add("destination_postcode", "50000");
        formData.add("length", request.length());
        formData.add("width", request.width());
        formData.add("height", request.height());
        formData.add("selected_type", Optional.ofNullable(request.selected_type()).orElse("1"));
        formData.add("parcel_weight", Optional.ofNullable(request.parcel_weight()).orElse(1D));
        formData.add("document_weight", request.document_weight());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        log.info("{} Request data:: {}", Provider.CITY_LINK, formData);
        log.info("{} Request headers:: {}", Provider.CITY_LINK, headers);
        var requestEntity = new HttpEntity<>(formData, headers);
        var response = restTemplate.exchange(
                "https://www.citylinkexpress.com/wp-json/wp/v2/getShippingRate",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );
        log.info("{} Response data:: {}", Provider.CITY_LINK, response);

        return response;
    }
}
