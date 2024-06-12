# Transformer File Service

# Pre-requisites to run project
- General
  - install docker and docker-compose
- For Production
  - create .env file
     ```
    ROOT_LOGGER_LEVEL=INFO
    APPLICATION_LOGGER_LEVEL=INFO
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
To init the mongodb service
 ```
 docker-compose up
 ```

To run the app
```
./gradlew bootRun
```

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

# Endpoint
Base url
```
http://localhost:8080
```

| Method | Endpoint                                     | Description                                                                       | Request Body                                     | Response Status           | Response Body                                                                                                       |
|--------|----------------------------------------------|-----------------------------------------------------------------------------------|--------------------------------------------------|---------------------------|---------------------------------------------------------------------------------------------------------------------|
| POST   | /api/v1/files/transform | Transform order data file in txt format in an normalized data file in JSON format | format form-data, key=word "file", value=data file | 201 Created               | Order data file in JSON format                                                                                      |
