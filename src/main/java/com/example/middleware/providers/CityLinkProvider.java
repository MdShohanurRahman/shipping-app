/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.GoodsType;
import com.example.middleware.enums.Provider;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class CityLinkProvider implements ShippingProvider {

    private static final List<String> originStateList = List.of(
            "Johor", "Kedah", "Kelantan", "Kuala Lumpur",
            "Melaka", "Negeri Sembilan", "Pahang", "Perak",
            "Perlis", "Pulau Pinang", "Sabah", "Sarawak",
            "Selangor", "Terengganu"
    );

    @Override
    public ShippingRateData getShippingRateData(ShippingRateRequest request) {
        ShippingRateData rateData = new ShippingRateData(Provider.CITY_LINK_EXPRESS);
        try {
            // sanitize data
            List<String> errors = checkErrors(request);

            if (errors.size() > 0) {
                rateData.setMessages(errors);
                return rateData;
            }

            ResponseEntity<Map> response = getApiResponse(request);

            if (response.getStatusCode().value() != 200) {
                rateData.setMessages(List.of("invalid response"));
                return rateData;
            } else {
                var responseBody = response.getBody();
                Map req = (Map) responseBody.get("req");
                Map data = (Map) req.get("data");

                rateData.setRate(String.valueOf(data.get("rate")));
                rateData.setMessages(List.of((String) data.get("message")));
            }

            return rateData;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("can't get data from {}", Provider.CITY_LINK_EXPRESS);
            rateData.setMessages(List.of(ex.getMessage()));
            return rateData;
        }
    }

    private ResponseEntity<Map> getApiResponse(ShippingRateRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // prepare formData
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("origin_country", "MY");
        formData.add("origin_state", request.origin_state());
        formData.add("origin_postcode", request.origin_post_code());
        formData.add("destination_country", request.destination_country());
        formData.add("destination_state", Optional.ofNullable(request.destination_state()).orElse(request.destination_country()));
        formData.add("destination_postcode", Optional.ofNullable(request.destination_postCode()).orElse("50000"));
        formData.add("length", request.length());
        formData.add("width", request.width());
        formData.add("height", request.height());
        formData.add("selected_type", GoodsType.valueOf(request.goods_type()).value());
        if (Objects.equals(request.goods_type(), GoodsType.PARCEL.name())) {
            formData.add("parcel_weight", request.weight());
        } else {
            formData.add("document_weight", request.weight());
        }
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        log.info("{} Request data:: {}", Provider.CITY_LINK_EXPRESS, formData);
        log.info("{} Request headers:: {}", Provider.CITY_LINK_EXPRESS, headers);
        var requestEntity = new HttpEntity<>(formData, headers);
        var response = restTemplate.exchange(
                "https://www.citylinkexpress.com/wp-json/wp/v2/getShippingRate",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );
        log.info("{} Response data:: {}", Provider.CITY_LINK_EXPRESS, response);

        return response;
    }

    private List<String> checkErrors(ShippingRateRequest request) {
        List<String> errors = new ArrayList<>();
        if (!originStateList.contains(request.origin_state())) {
            errors.add(request.origin_state() + " is not valid origin_state");
        }

        if (Objects.equals(request.goods_type(), GoodsType.PARCEL.name())) {
            // if parcel then length, width, height is required
            if (request.length() == null || request.length() <= 0) {
                errors.add("length can not be 0");
            }
            if (request.width() == null || request.width() <= 0) {
                errors.add("width can not be 0");
            }
            if (request.height() == null || request.height() <=0) {
                errors.add("height can not be 0");
            }
        }

        return errors;
    }
}
