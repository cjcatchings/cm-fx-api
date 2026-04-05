# Currency Exchange API - Crewmeister Test Assignment Submission

This submission contains a currency exchange API between the 
Euro and other currencies.  As part of this code challenge, I 
built a RESTful API that allows users to perform the following actions:

- Retrieve a list of available currencies for exchange rate retrieval
- Retrieve daily conversion rates (from EUR to the requested currency)
- Retrieve a conversion rate (from EUR to the requested currency) for a specified day
- Convert an amount of a given currency on a given day to Euros (EUR)

The API was built using Spring Boot and includes the following Spring projects

 - Spring Framework Web Controller
 - Jakarta Persistence
 - H2 Database
 - Spring Batch

## Background

As part of this code challenge, I was given the following requirements:

1. As a client, I want to get a list of all available currencies
2. As a client, I want to get all EUR-FX exchange rates at all available dates as a collection
3. As a client, I want to get the EUR-FX exchange rate at particular day
4. As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day

The [Bundesbank Daily Exchange Rates](https://www.bundesbank.de/dynamic/action/en/statistics/time-series-databases/time-series-databases/759784/759784?statisticType=BBK_ITS&listId=www_sdks_b01012_3&treeAnchor=WECHSELKURSE) was provided as a data source for historical EUR->FX conversion rates.  I used this as a data source for my application for the following curencies to start:

 - United States Dollar (USD)
 - Australian Dollar (AUD)
 - Canadian Dollar (CAD)
 - Swiss Franc (CHF) - Pending
 - British Pound (GBP) - Pending
 - Japanese Yen (JPY) - Pending

As requirements 2 and 3 indicate conversion from EUR-to-FX, I return all exchange rates in the amount that one (1) Euro would be exchanged.

As requirement 4 indicates that a foreign currency amount is to be exchanged **TO** Euros, I divided the amount of foreign currency by the given rate to calculate the amount of Euros it converts to.

## How to Run

Running this application requires the following:

 - Runtime:  Java 21
 - Maven:  3.x

First, ensure that your `JAVA_HOME` and `PATH` points to a JDK of version 21.

Next, download the Maven dependencies in this project.  I did this in IntelliJ IDEA via the `pom.xml` file.

To run the application, execute the following:

`mvn spring-boot:run`

This will start an H2 database and execute a Spring Batch job that loads currencies in `src/main/resources/currencies.csv` as well as exchange rates in CSV files in the `src/main/resources/exchangerates` folder.

To execute unit tests for this application, run the following:

`mvn test`

After your run the unit tests, generate HTML code coverage reports, run the following:

`mvn jacoco:report`

## Controller/API Endpoints

This application provides the following RESTful API endpoints:

### `GET /api/currencies`

This endpoint retrieves a list of available currencies in the system with which a user can look up historical conversion rates from the Euro.

The data is returned in the HTTP response body in the following JSON format:

```
...
    {
        "code": "USD",
        "name": "United States Dollar"
    },
    {
        "code": "AUD",
        "name": "Australian Dollar"
    },
...
```

The output fields are as follows:
 - `code` - the known 3-letter code for the given currency
 - `name` - a descriptive name of the currency

### `GET /api/currencies/{code}`

This endpoint retrieves daily historical conversion rates from one (1) Euro to the requested currency.

The following input is required:
 - `code` - A path variable that represents the requested 3-letter currency code (ex. `USD`)

The output is returned in the following format:
```
...
    {
        "date": "2026-04-01",
        "rate": 1.1605
    },
    {
        "date": "2026-03-31",
        "rate": 1.1498
    },
...
```
The output is returned in the HTTP response body as a list of historical conversion rates:

 - `date` - The day (in `yyyy-MM-dd` format) that the given conversion rate was provided
 - `rate` - The conversion rate of one (1) Euro to the given currency on the corresponding date

This endpoint can return the following non-OK (200) HTTP response codes

 - `404` - Returned if the provided currency `code` is not available in the API

### `GET /api/currencies/{code}/{date}`

This endpoint returns a single conversion rate for a given foreign currency (from Euro) on a given date.  The following inputs are required:

 - `code` - A path variable that represents the requested 3-letter currency code (ex. `USD`)
 - `date` - A path variable that represents the requested day (in `yyyy-MM-dd` format) for the conversion rate on that day

The output is returned in the HTTP response body as a single JSON object:

```
{
    "date": "2026-03-25",
    "rate": 1.1592
}
```
The JSON object contains the following attributes:

- `date` - The day (in `yyyy-MM-dd` format) that the given conversion rate was provided
- `rate` - The conversion rate of one (1) Euro to the given currency on the corresponding date

This endpoint can return the following non-OK (200) HTTP response codes:

 - `400` - The provided `date` path variable is not in a valid `yyyy-MM-dd` format
 - `404` - Either the currency `code` provided is not available in the API or there is no conversion rate for the given `date` (typically due to the day falling on a weekend or holiday)

### `GET /api/currencies/{code}/{date}/convert`

This endpoint converts a given amount of foreign currency into Euros on a given day.

The following inputs are required from the user:

- `code` - A path variable that represents the requested 3-letter currency code (ex. `USD`)
- `date` - A path variable that represents the requested day (in `yyyy-MM-dd` format) for the conversion rate on that day
- `conversionRequestDto` - A JSON object in the request body that should be the following format:
```
{
    "sourceAmount": 2300
}
```
`sourceAmount` is the amount of the requested foreign currency to convert to Euros.

The output is returned in the HTTP response body as a single JSON object:
```
{
    "rateUsed": 1.1592,
    "amountInEuros": 1984.13
}
```
The single JSON object contains the following attributes:
 - `rateUsed`:  The EUR-to-FX rate used to convert the `sourceAmount` into Euros
 - `amountInEuros`:  The amount in Euros that the given `sourceAmount` converted to on the given day

This endpoint can return the following non-OK (200) HTTP response codes:

- `400` - The provided `date` path variable is not in a valid `yyyy-MM-dd` format or the request body is invalid
- `404` - Either the currency `code` provided is not available in the API or there is no conversion rate for the given `date` (typically due to the day falling on a weekend or holiday)


## Technical Layers

For the API I used a conventional controller/service/repository separation of concerns to separate each layer.

The controller layer acts as the entrypoint to the API to process requests and return responses

The service layer handle any business specific logic, converts backend entity abstractions to serializable DTOs and 
interacts with the repository layer on behalf of the controller.

The repository layer performs the necessary database (H2) transactions required to retrieve persisteed currency and conversion rate data.

The application also separates mappers (entity-to-DTO and vice versa), serializable DTOs, utility classes and application specific exceptions.

Lastly, the application includes a Spring Batch Job that loads currency and conversion rate data from provided CSV files (in `src/main/resources`).  This defined as a Spring Batch Configuration with the following resources:
 - `FlatFileItemReaders`
 - `ItemProcessors`
 - `ItemWriters`

### Controller

As previously mentioned, the `SpringBootController` acts as the API entrypoint and defines the endpoints to be called by users.

Methods in this layer handle requests from users and seralizes/sends responses to them.

As all endpoints use the HTTP `GET` method, all responses return an `HTTP 200 OK` response code.

Best practices regarding HTTP client exception codes are taken in consideration:

 - `404` - Not found - typically returned when a resource (currency, conversion rate on a given day) is not found
 - `400` - Bad request - typically returned when invalid input data is provided in a request

### Services

This application implements abstractions for two service layers in `com.crewmeister.cmcodingcallenge.currency.service`

 - `CurrencyService` - Retrieves currency specific data (name/code)
 - `ConversionRateService` - Retrieves conversion rate specific data and performs currency conversion calculations

All services are annotated with the Spring `@Service` annotation for effective dependency injection.

### Repositories/Persistence Layer

An H2 database is used as the persistence layer to store currency and conversion rate data.  Data is loaded at startup and torn down at shutdown.

The Jakarta Persistence (JPA) library is used to provide methods that can perform queries against the H2 database.  These methods are called from the service layer.

The persistence layer contains the following entities/fields/indices

 - `Currency`
   - `id` - An auto-generated (by JPA) unique ID for the given currency
   - `code` - Known 3-letter code for the given currency
     - Indexed due to frequent queries against this column
   - `name` - Descriptive name of the currency
 - `ConversionRate`
   - `currency` - A foreign key back to `Currency` that represents the currency for this conversion rate
   - `date` - A temporal date that represents the day for this conversion rate
   - `rate` - The conversion rate

`ConversionRate` uses a composite ID with a unique combination of currency and date to represent a unique entry for each combination.

### Mappers

[MapStruct](https://mapstruct.org/) is used to map JPA entities to serializable DTOs for the `RestController` to return.

### Spring Batch Job

A Spring Batch Job named `importCurrencyAndConversionRatesJob` loads currencies and conversion rate data into the H2 database at startup.

This Spring Batch Job is defined in `com.crewmeister.cmcodingchallenge.currency.config.BatchConfiguration`

The Job contains the following steps:

#### `importCurrenciesStep`

This step loads the currencies provided in `resources/currencies.csv` into the H2 database.  It uses a conventional reader/process/writer pattern to load/process/import into the database.

 - `FlatFileItemReader` - Reads `currencies.csv` and loads each row as a DTO
 - `CurrencyLoaderProcessor` - A Spring Batch `ItemProcessor` implementation that maps CSV record items to the `Currency` entity attributes
 - `RepositoryItemWriter` - Writes the generated `Currency` entities to the database

#### `importConversionRatesStep`

This step loads teh conversion rates provided in the collection of CSV files in `resources/exchangerates`.

 - `MultiResourceItemReader` - Reades multiple file resources in `resources/exchangerates` and converts them into DTOs for processing
 - `CurrencyAwareFlatFileItemReader` - A `FlatFileItemReader implementation that maps `Currency` based on file name data for each row in the file
 - `ItemProcessor` - Processes conversion rate CSV records into the `ConversionRate` entity
 - `RepositoryItemWriter` - Writes the generated `ConversionRate` entities to the database

## Testing

JUnit Testing is used to implement unit tests at the controller and service layers for this application.

[JaCoCo](https://github.com/jacoco/jacoco) is used to generate code coverage reports for unit tests runs on this application.

Currently, unit test coverage is at approximately 87%.

## Considerations

### Persistence

I decided to use an H2 database as a persistence layer to utilize peripheral projects to Spring such as JPA that facilitate CRUD-based operations.
At the moment, I have only loaded USD, AUD and CAD for this project.  However, other [Bundesbank Daily Exchange Rates](https://www.bundesbank.de/dynamic/action/en/statistics/time-series-databases/time-series-databases/759784/759784?statisticType=BBK_ITS&listId=www_sdks_b01012_3&treeAnchor=WECHSELKURSE) can be added as CSV files to the `resources/exchangerates` folder.  Make sure the currency is also added to `currencies.csv`.  Some manual 'pre-processing' was done to remove extraneous headers from the Bundesbank's CSV files.  Removing those should allow the records to load successfully.

### Use of constructors over `@Autowired`

The project favors use of constructors for Spring-based dependency injection over the `@Autowired` annotation.  This was motivated by the following article:

[Why is Field Injection not recommended?](https://www.baeldung.com/java-spring-field-injection-cons)

IntelliJ also raises warnings for many uses of `@Autowired`.  For this I decided to use the constructor pattern to inject class dependencies.

### Batch Configuration Unit Test

The unit test in `com.crewmeister.cmcodingchallenge.currency.config.BatchConfigurationTests` deletes and re-creates all `Currency` and `ConversionRate` records.  This may not be an effective 
mechanism of unit testing the `BatchConfiguration` class.  Perhaps if I had more time, I would determine a more appropriate mechanism for unit testing this class.

### Cutoff Date

As of this time, exchange rates up to April 1, 2026 are loaded as part of application startup.

## If I had more time...

the `GET /api/currencies/{code}` endpoint returns all conversion rates for a given currency, which can contain payloads of 250kB or more.  These payloads get larger as future dates are loaded.

If I find time on Monday April 6, I may implement the following to make this more "production grade":

 - Implement query parameters for this endpoint to provide a "to" and "from" date to reduce the number of conversion rates returned
 - Provide a system-level configuration that limit the number of results at a system level

I also would have loaded additional currencies.