## Kalah
A simple web-app for [Kalah Game](https://en.wikipedia.org/wiki/Kalah)

### Technologies
- Java 8
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Lombok](https://projectlombok.org)
- [Redis](https://redis.io/)
- [Spring Data Redis](https://spring.io/projects/spring-data-redis)
- [JUnit](https://junit.org/)
- [Swagger](https://swagger.io/)

### REST api
| METHOD | PATH | Description | Parameters | 
| -----------| ------ | ------ | ----- |
| POST | /games | create game | - ||
| PUT | /games/{gameId}/pits/{pitId} | move on a game | gameId and pitId (integer) ||

http://localhost:8080/swagger-ui.html#


### Data in Redis
Using RedisHash created by spring-data-redis


### How to build
```sh
cd kalah
./mvnw clean package
```

### How to run
Only need docker for run.
```sh
docker-compose up
curl --header "Content-Type: application/json" --request POST http://localhost:8080/games
curl --header "Content-Type: application/json" --request PUT http://localhost:8080/games/1/pits/2
```
### How to run tests
```sh
cd kalah
./mvnw clean test
# converage file: ./target/site/jacoco/index.html
# be sure that 8080 and 6379 ports are not in use, before building or running testes.
``` 
