# **Shipping Provider Middleware**

This project is a middleware that provides shipping rate comparison between two shipping providers: City Link Express and JT Express. Users can submit a request with specific details of their shipment, and the middleware will return rates for both providers.

**How it works**

The middleware receives a request with the following payload:

```json
{
  "origin_country": "MY",
  "origin_state": "Johor",
  "origin_post_code": "80100",
  "destination_country": "ID",
  "destination_state": "string",
  "destination_postCode": "string",
  "goods_type": "PARCEL",
  "expressDelivery": false,
  "length": 1,
  "width": 1,
  "height": 1,
  "weight": 1,
  "insurance_item_value": "10",
  "providers": [
    "CITY_LINK_EXPRESS",
    "JT_EXPRESS"
  ]
}
```


The middleware will then compare shipping rates between City Link Express and JT Express. For City Link Express, it will call the provider's public API to retrieve the rate. For JT Express, it will scrape the data from the provider's website using Selenium.

The middleware will return a response with the following payload:

```json
{
  "data": [
    {
      "provider": "JT_EXPRESS",
      "rate": "69.79",
      "messages": []
    },
{
      "provider": "CITY_LINK_EXPRESS",
      "rate": "50",
      "messages": []
    }
  ]
}
```

Users can see the rate and other details for each provider in the response payload. If there is errors or some messages then it will append messages list. I make the response paylod very simple so that user can easily decide that which provider is suitable for him.

**Installation**

To run this project, you will need to have Docker installed on your machine. Clone the repository and run the following command in the project directory:

```bash
docker-compose up
```


**Usage**

To access the [`/api/v1/shipping-rates`](http://localhost:8080/api/v1/shipping-rates) API, you need to first generate a token and then add it to the header of the request. Here are the steps you need to follow:

1. Send a `POST` request to `/api/v1/auth/login`API with your email and password to get the token. I added an admin user for dev mode. use email as **admin@gmail.com** and password as **admin**

2. Once you get the token, add it to the header of the `GET` request to `/api/v1/shipping-rates` API. You can add the token to the `Authorization` header with the value `Bearer <token>`.


## **Contributors**

* **Shohanur Rahman** - Creator and maintainer of the project.