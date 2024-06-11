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

## Format Code Style
To check code style
- ./gradlew spotlessCheck


To apply code style suggestions
- ./gradlew spotlessApply

## Testing Metrics
To run mutation tests
- ./gradlew pitest
