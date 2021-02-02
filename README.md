# CodeSupport Website Backend

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/448f0a0fbf14480ca9735246d3ade25f)](https://app.codacy.com/gh/codesupport/website-backend?utm_source=github.com&utm_medium=referral&utm_content=codesupport/website-backend&utm_campaign=Badge_Grade_Settings)

## About
This repository contains the code for the backend of the CodeSupport website. The project is written in Java using the Spring framework. 

Documentation is powered by Swagger and can be reached at `/swagger-ui.html` when running the application locally.

## Dependencies
Please see [pom.xml](https://github.com/codesupport/website-backend/blob/develop/pom.xml) for a list of dependencies.

## Setup
1. Navigate into the repository on your computer
2. On Unix systems run `./mvnw clean package` and on Windows run `./mvnw.cmd clean package`
3. Run `java -jar target/api-service-0.0.1-SNAPSHOT.jar`

## Tests
We are using [JUnit](https://junit.org/junit4/) for our tests. **All code should be tested**.

## Integration Tests
Integration tests are written with postman and can be excuted through the postman GUI or via the CLI utility, `newman`.

`newman run -e postman/Local.postman_environment.json postman/CodesupportApi.postman_collection.json`

## Scripts
- To delete the `target/` build folder use `./mvnw clean`
- To compile `.class` files, run tests and package into `.jar` use `./mvnw package`
- To compile, package and install to a local m2 repo use `./mvnw install`
- To run unit tests use `./mvnw test`
- To delete the `target/` build folder and repackage use `./mvnw clean package` (this is the prefered way to build)
- To run the application use `java -jar target/api-service-0.0.1-SNAPSHOT.jar`

## Environment Variables
Name | Default | Description
---|---|---
`API_HOSTNAME` | | The hostname of the backend API (https://api.codesupport.dev)
`COOKIE_NAME` | | The name of the auth cookie
`COOKIE_MAX_AGE` | | The max age of the auth cookie in seconds
`CORS_ORIGIN` | | Origin used for CORS header
`DATABASE_URL` | | The URL to the database
`DATABASE_USERNAME` | | The username for the database
`DATABASE_PASSWORD` | | The password for the database
`DATABASE_DRIVER` | `com.mysql.jdbc.Driver` | The driver for the database
`DATABASE_DIALECT` | `org.hibernate.dialect.MySQLDialect ` | The dialect for the database
`DATABASE_POOL_SIZE` | `4` | The maximum number of database connections
`DATABASE_POOL_IDLE` | `2` | The minimum number of database connections to keep open
`DISCORD_APP_ID` | | The Discord app's ID
`DISCORD_APP_SECRET` | | The Discord app's secret
`DISCORD_APP_REDIRECT` | | The Discord app's redirect for OAuth
`DISCORD_LOG_WEBHOOK_URL` | | Webhook url for posting logs to discord
`DISCORD_LOG_USERNAME` | ApiBackend | The username used in the discord log embed messages
`HC_PING_DELAY` | `5000` | Delay between healthchecks (ms)
`HC_PING_URL` | | Url of the external healthcheck api to hit
`LOG_ROOT_LEVEL` | `INFO` | Root logging level for spring logs
`MAX_IMAGE_SIZE` | `512000` | Max image upload size (in bytes)
`SERVICE_PORT` | `8080` | The port to run the application on
`SSL_KEY_ALIAS` | `tomcat` | Alias for certificate, used with openssl step
`SSL_KEY_STORE` | | Path to the keystore (/etc/.../keystore.p12)
`SSL_KEY_STORE_PASSWORD` | | Password to access keystore
`SSL_KEY_STORE_TYPE` | `PKCS12` | Format for keystore (Spring likes PKCS12)

**Any Questions?** Feel free to mention @LamboCreeper#6510 in the [CodeSupport Discord](https://discord.gg/Hn9SETt).
