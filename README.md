## Menu API

CRUD application responsible for customer management.

[![Build Status](https://travis-ci.org/akashnimare/foco.svg?branch=master)](https://travis-ci.org/akashnimare/foco)
[![Windows Build Status](https://ci.appveyor.com/api/projects/status/github/akashnimare/foco?branch=master&svg=true)](https://ci.appveyor.com/project/akashnimare/foco/branch/master)

## Code style

[![js-standard-style](https://img.shields.io/badge/code%20style-standard-brightgreen.svg?style=flat)](https://github.com/feross/standard)

| Stack      | Version |
|------------|---------|
| Java       | 17      |
| Springboot | 2.4.2   |
| Gradle     | 7.4.2   |
| Flyway     | 5.2.4   |
| MySQL      | 8.0     |

## API Reference

http://localhost:8080/swagger-ui.html

## Tests

Tests were implemented using the libraries MockMVC and Wiremock. To run them, simply build the application.
## How to use ?
Build the project
```sh
./gradlew clean build
```

Start dependencies
```sh
docker-compose up -d
```

Start application
```sh
./gradlew bootRun --args='--spring.profiles.active=local'
```

## License

Japanet
