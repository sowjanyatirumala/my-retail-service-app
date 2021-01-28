# Spring Boot "myRetailService" REST API Project

The purpose of the project is to create myRetail RESTful webservice that aggregates product data from multiple sources and return it as JSON to the end user.
The application performs the following actions.
* Responds to HTTP GET request at /products/{id} and delivers product data as JSON (where {id} is a number.
   * The product name is retrieved from an external API "https://redsky.target.com/v3/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate" where {id} is a number denoting the product ID.
   * The product price is retrieved from NoSQL data store (Cassandra) along with the currency code for the desired product ID.
   * The results of the two operations are combined with the product ID to frame a JSON response in the following format:
```
{"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}
```   

* Accepts HTTP PUT request at the same path /products/{id} containing a JSON request body similar to the above GET response and updates the price in the data store.

##How to run
This application is packaged as a war using Gradle. No Tomcat or Gradle installation is necessary. 

* Clone this repository
* Make sure you are using JDK 1.8
* Gradle wrapper is used in the project so you don't need to have Gradle installed on your local machine.
* Swagger is used in the project for documentation purposes.  
  You can build the project and run the tests by running ```gradle clean build```  
* Once successfully built, you can run the service by one of these two methods:
```
        gradle bootrun
```
        
* For accessing the endpoints, use the swagger page at http://localhost:8085/api/swagger-ui

##About the service
* The service is  simple REST service using Jersey. It uses RestTemplate to retrieve the product name using GET from redsky external API.
* Spring Boot 2 is used for implementing the project which avoids most of the explicit configuration needed for the dependencies like Jersey, swagger or spock, etc.
* Spock is used for unit and integration testing.
* Swagger is integrated for meaningful documentation for the API.  
* ###Cassandra
Cassandra is used as NoSql database for storing the product price for a specific ID.
* You need to have Cassandra setup in your local for the service to work as expected.
* Download Cassandra from https://cassandra.apache.org/download/.
* Extract the installation to the desired location using the command:
```tar -xzvf apache-cassandra-3.0.19-bin.tar.gz```
*  To start Cassandra, run the command:
```
cd install_location
bin/cassandra //starts cassandra
```
* By default, Cassandra provides a prompt Cassandra query language shell (cqlsh) that allows users to communicate with it. Using this shell, you can execute Cassandra Query Language (CQL). The shell utility can be run using the following command from the installation's bin folder.
```
  cqlsh
```

* Using cqlsh, you can define a schema, insert data, and execute a query (refer to https://www.tutorialspoint.com/cassandra/cassandra_cqlsh.htm for more information on cqlsh.
* Create cassandra keyspace 'myRetailKeyspace' using the command:
```
cqlsh> CREATE KEYSPACE myRetailKeyspace
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
```
* The webservice works as expected when the cassandra is up and running.
* TODO - The above steps can be eliminated by using docker container for local cassandra setup. This is not implemented yet.

##Endpoints
###Update product price in the data store
```
PUT /api/products/{id}
Accept: application/json

{
  "id": 13860428,
  "name": "string",
  "currentPrice": {
    "currencyCode": "USD",
    "value": 250.0
  }  
}

RESPONSE: HTTP 204 (Product price saved)
Example: http://localhost:8085/api/products/13860428
```
###Get the product details
```
GET /api/products/{id}
Accept: String
Produces: application/json

Example response: Product found in the store
{
  "id": 13860428,
  "name": "The Big Lebowski (Blu-ray) (Widescreen)",
  "currentPrice": {
    "currencyCode": "USD",
    "value": 250.0
  }  
}

RESPONSE: HTTP 200 (Product found in the store)
Example: http://localhost:8085/api/products/13860428
```




        

   