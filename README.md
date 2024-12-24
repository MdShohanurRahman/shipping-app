# **Shipping Rate Comparison App**

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

**Running Locally (Docker Users)**

If you have docker installed in your machine then follow these steps

Clone the repository to your local machine using Git

```bash
git clone https://gitlab.com/shohan.drmc/shipping-app.git
```

Navigate to the project directory

```bash
cd shipping-app
```

Uncomment `spring.profiles.active=docker` this line in your `src/main/resources/application.properties`  
before build your project

build the project and generate the jar file running the following command

```bash
mvn clean install
```

Run the following command to start up your application:

```bash
docker-compose up
```

Once the containers are up and running, you should be able to access your application by visiting [**http://localhost:8080**](http://localhost:8080) in your web browser.

If you want to stop the application, simply run the following command:

```bash
docker-compose down
```

Note that if you make any changes to your code, you may need to rebuild your Docker images using the `docker-compose build` command before running `docker-compose up` again.

###   
**Running Locally**

Clone the repository to your local machine using Git

```bash
git clone https://gitlab.com/shohan.drmc/middleware-app.git
```

Navigate to the project directory

```bash
cd middleware-app
```

Make sure you have Java 17 installed on your machine. You can verify the version by running the following command:

```bash
java -version
```

Create a new database named `middleware-db` in your MySQL database.

Configure the database connection in `src/main/resources/application.properties`

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/middleware-db
spring.datasource.username=<your-db-username> 
spring.datasource.password=<your-db-password> 
```

Run the project using the following command

```bash
mvn spring-boot:run
```

The project should now be running on [`http://localhost:8080`](http://localhost:8080).


**Usage**

To access the [`/api/v1/shipping-rates`](http://localhost:8080/api/v1/shipping-rates) API, you need to first generate a token and then add it to the header of the request. Here are the steps you need to follow:

1. Send a `POST` request to `/api/v1/auth/login`API with your email and password to get the token. I added an admin user for dev mode. use email as **admin@gmail.com** and password as **admin**

2. Once you get the token, add it to the header of the `GET` request to `/api/v1/shipping-rates` API. You can add the token to the `Authorization` header with the value `Bearer <token>`.


## **Contributors**

* **Shohanur Rahman** - Creator and maintainer of the project.
