# q-url-api [![Build Status](https://travis-ci.org/kujawad/q-url-api.svg?branch=master)](https://travis-ci.org/kujawad/q-url-api)

Quick url shortener api provides functionality for shortening long urls.

# Requirements
- Java 11
- Any database supported by Liquibase (see [Application flow](#application-flow))

# Swagger documentation
Swagger documentation is available at [https://q-url-api.herokuapp.com/swagger-ui/](https://q-url-api.herokuapp.com/swagger-ui/).

After running application, the documentation will be available at `<host>/swagger-ui/` (eg. `http://localhost:8080/swagger-ui/`).

# Prerequisites
The best way is to isolate applications using containers (see [Docker](#docker)). If You don't want to use
Docker proceed with the following subsections.

This version of source code requires the PostgreSQL database. You can change the database (see [Application flow](#application-flow)).

## PostgreSQL
Since the ***q-url-api*** application uses PostgreSQL you have to set up these:
- database: `q-url-api`
- username: `postgres`
- password: `postgres`
- port: `5432`
- extension: `uuid-ossp`

> These can be set up by yours intention, but later you have to change 
> the `spring.datasource.url`, `spring.datasource.username` and `spring.datasource.password` properties in
> `application-local.properties`.

## application.properties
Now all you have to do is change the spring active profile from `dev` to `local`:

The `application.properties` file should look like this:
```properties
# ===============================
# = SPRING PROFILE
# ===============================
spring.profiles.active=local
```

# Running
The ***q-url-api*** is based on Gradle, so all you have to do is:

Linux:
```shell script
./gradlew bootRun
```

Windows:
```shell script
.\graldew.bat bootRun
```

# Building jar
In order to build .jar You have to execute the following command:

```shell script
./gradlew clean build
```

The `*.jar` archive will be available in the `build/libs` directory.

# Application flow
Since ***q-url-api*** is based on [Spring Boot](https://spring.io/projects/spring-boot) it uses the 
*property files* to configure for example `DataSource` (for more information see [Spring Boot documentation](https://docs.spring.io/autorepo/docs/spring-boot/current/reference/html/spring-boot-features.html#boot-features-external-config)).

The ***q-url-api*** uses the *database schema changes* tool called [Liquibase](https://www.liquibase.org/), 
in order to make the application database-independent.

If you want to use a different database or make changes to the database see [Liquibase](#liquibase).

# Liquibase
If you would want to change the database You have to make sure that [Liquibase supports it](https://www.liquibase.org/get-started/databases).

If Liquibase supports given database, the change is possible by importing appropriate database driver in 
`build.gradle` file.

## Change database
In case of the ***q-url-api*** application, which uses PostgreSQL these lines are importing drivers:

`build.gradle`:
```groovy
...
// PostgreSQL
runtimeOnly 'org.postgresql:postgresql'

...
// Liquibase
liquibaseRuntime 'org.postgresql:postgresql'
```

And these should be changed too in order to change the database:

`src/main/resources/db/changelog/db.changelog-master.yaml`:
```yaml
databaseChangeLog:
  - property:
      name: uuid_type
      value: uuid
      dbms: postgresql
  - property:
      name: uuid_function
      value: uuid_generate_v4()
      dbms: postgresql
```

The properties under the `property` variables should be modified for the corresponding databases in order
to generate UUIDs properly. See [Liquibase reference](https://docs.liquibase.com/workflows/liquibase-community/working-with-uuids.html?Highlight=uuid).

## Database changes
If you want to somewhat interfere with the database schema, the 
`src/main/resources/db/changelog/db.changelog-master.yaml` file is Your friend.
See [Liquibase reference](https://docs.liquibase.com/home.html) for more information.

## Liquibase Gradle Plugin
Liquibase makes it easy to modify the database schema, but 
[Liquibase Gradle Plugin](https://github.com/liquibase/liquibase-gradle-plugin) makes it even easier.

Everytime you change the `db.changelog-master.yaml` file all you have to do is:

```shell script
./gradlew update
```

in order to apply the changes to database.
# Docker
I highly recommend using [Docker](https://www.docker.com/) for at least database in order to isolate 
the application logic.

For the sake of the example, the Docker container and the database will be named **q-url-api**.

Docker postgres container:
```shell script
docker run --name q-url-api -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

If you want to create the postgres container with appropriate database and *uuid-ossp*
extension execute the following command:
```shell script
docker exec q-url-api /bin/sh -c "psql -c 'CREATE DATABASE \"q-url-api\";' -U postgres && psql -d \"q-url-api\" -c 'CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";' -U postgres"
```