# Exchange Rate

## Usage
1. Make sure you have [SBT build tool](https://www.scala-sbt.org/download.html) installed
2. Build the Docker image using the command: `sbt docker:publishLocal`
3. Run the application in Docker: `docker run --rm hsa-exchange-rate:1.0`
4. The application will be sending requests every 10 seconds and printing to console.

Console output example:
```
$ docker run --rm hsa-exchange-rate:1.0
Bank URL: https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=USD&json
Analytics URL: https://www.google-analytics.com/mp/collect?api_secret=FjrqsjKeRZKHqJvAkow9zA&measurement_id=G-S9NP3WB7KH
Sending requests every 10 seconds
Serialized response from bank: List(BankExchangeRate(840,Долар США,36.5686,USD,24.02.2023))
Sending JSON to Analytics:{"client_id":"123456789.123456789","events":[{"name":"usdtouah","params":{"rate":36.5686}}]}
Analytics response body: 
Analytics response code: 204

```