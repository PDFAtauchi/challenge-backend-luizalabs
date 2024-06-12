# Transformer File Service

<center>
This project aims to integrate two systems via a REST API that convert 
denormalized order data file into normalized order data file for a consumer system. 
It also provides storage for the normalized data files, enabling easy querying of 
orders using various filters.
</center>

# Pre-requisites to run project
- General
  - install docker and docker-compose
    - For Production
      - create .env file in the root of the project, e.g.
         ```
        ROOT_LOGGER_LEVEL=INFO
        APPLICATION_LOGGER_LEVEL=INFO
    
        MONGO_INITDB_ROOT_USERNAME=usern
        MONGO_INITDB_ROOT_PASSWORD=userp
        MONGO_INITDB_DATABASE=testdb
    
        MONGO_USER=usern
        MONGO_PASS=userp
        MONGO_HOST=mongo_db
        MONGO_PORT=27017
        MONGO_DB=testdb
        MONGO_AUTH_SOURCE=admin
        ```
- For Development
    - install python >= 3.8 (for pre-commit checkers)
    - python -m pip install pre-commit
    - pre-commit install
    - pre-commit install --hook-type commit-msg
    - to connect with a mongodb service, it is up a mongodb service via docker-compose.yml, you can update the values of mongodb uri in application.properties i.e.
      ```spring.data.mongodb.uri=mongodb://usern:userp@localhost:27017/testdb?authSource=admin```

# For Run Code
## In development
To init and stop the mongodb service
 ```
 docker-compose up
 ```
 ```
 docker-compose down -v
 ```

To run the app
```
./gradlew bootRun
```

## In Production
To init and stop project
````
docker-compose -f docker-compose.prod.yml up --build
````
````
 docker-compose down -v
````

## Format Code Style
To check code style
```
./gradlew spotlessCheck
```

To apply code style suggestions
```
./gradlew spotlessApply
```

## Testing Metrics
To run mutation tests
```
./gradlew pitest
```

To run normal tests
```
./gradlew test
```

# Endpoints
### API To check the health of the app
Base url
```
http://localhost:8080
```

| Method | Endpoint     | Description                       | Request Body                                     | Response Status | Response Body                                                                                                       |
|--------|--------------|-----------------------------------|--------------------------------------------------|-----------------|---------------------------------------------------------------------------------------------------------------------|
| GET    | /healthcheck | Check the availability of the app |  | 200 OK          |                                                              {message: OK}                         |


### API to transform a denormalized data file into a normalized data file
Base url
```
http://localhost:8080
```

| Method | Endpoint                                     | Description                                                                              | Request Body                                            | Response Status | Response Body                                                                     |
|--------|----------------------------------------------|------------------------------------------------------------------------------------------|---------------------------------------------------------|-----------------|-----------------------------------------------------------------------------------|
| POST   | /api/v1/files/transform | Transform a valid order data file in txt format in a normalized data file in JSON format | format form-data, key=word "file", value=data file      | 201 Created     | Order data file in JSON format                                                    |
| POST   | /api/v1/files/transform | Return a message error when invalid data is trying to transformer                        | format form-data, key=word "file", value=json data file | 404 Bad Request | {"message": "Invalid file type, it needs to be .txt type"}                        |


### API to search orders in the normalized data. It can be filter by orderId and/or startDate and/or endDate
Base url
```
http://localhost:8080
```

| Method | Endpoint                          | Description                                              | Request Body                                            | Response Status | Response Body                           |
|--------|-----------------------------------|----------------------------------------------------------|---------------------------------------------------------|-----------------|-----------------------------------------|
| GET    | /api/v1/orders/search?orderId=836 | will search and return the list of orders in JSON format |      | 200 OK          | Lista de Orders filtered by the filters |