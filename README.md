# microservice

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/microservice-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Configuraciones adicionales

### Swagger 
http://localhost:8080/swagger-ui/

### Open Api
http://localhost:8080/openapi

## API Prometeus
la dependencia quarkus-micrometer-registry-prometheus habilita la exportacion de metricas para prometeus
http://localhost:8080/q/metrics
## Estructura del proyecto

- domain
  -   Contiene las entidades de dominio
- application.port.input
  -    Piertos de Entrada ClaseUseCase
- application.port.output
  -   ClaseRepository
- application.service
  -   Lógica de negocio ClaseService
- adapter.input.rest
Contien el API Rest ClaseResource
- adapter.output.persistence
  -   Implementacion del repositorio en memoria
InMemoryClaseRepository

## Crear yml de prometheus
```yml
# my global config
global:
 scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
 evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
 # scrape_timeout is set to the global default (10s).

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
# - "first_rules.yml"
# - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
 # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
 - job_name: 'prometheus'
   # metrics_path defaults to '/metrics'
   # scheme defaults to 'http'.
   static_configs:
     - targets: ['127.0.0.1:9090']

 - job_name: 'quarkus-micrometer'
   metrics_path: '/q/metrics'
   scrape_interval: 3s
   static_configs:
     - targets: ['HOST:8080']
```

## Prometheus Docker
```
docker run -d --name prometheus -p 9090:9090 -v <PATH_TO_PROMETHEUS_YML_FILE>/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
```


## Grafana Docker
```
docker run -d --name grafana -p 3000:3000 grafana/grafana
```

## Check
[X] Programación reactiva `Uni<T>`
[] `@Authenticated` para asegurar que todas las rutas requieran autenticación.
[X] `@RolesAllowed("ROLE_ADMIN")`  configurar peticiones por rol.
[ ] `@Valid @ValidatePayload` funcionan con version anterio de rest easy
[ ] SOAPConnection pendiente para clientes soap
[X] `@CacheResult(cacheName = "user-cache") ` Configuracion de cache quarkus.cache.user-cache.size=1000
quarkus.cache.user-cache.expire-after-write=5m
[X] `@Path("/cb/{id}") @Retry(maxRetries = 3, delay = 1000) @Fallback(fallbackMethod = "defaultUser") @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000) @Fallback(fallbackMethod = "fallbackGetUserById")`
[X] Valid Payload
[] Logging
[] Tests
[] Criptografia
[] Hibernate & Panache
