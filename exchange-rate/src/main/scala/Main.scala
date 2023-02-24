import cats.effect.{ExitCode, IO, IOApp, Temporal}
import sttp.client3._
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import sttp.client3.upicklejson._
import upickle.default._

import scala.concurrent.duration.DurationInt

object Main extends IOApp {

  private val bankUri = uri"https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=USD&json"

  private val analyticsQueryParams = Map("api_secret" -> "FjrqsjKeRZKHqJvAkow9zA", "measurement_id" -> "G-S9NP3WB7KH")
  private val analyticsUri = uri"https://www.google-analytics.com/mp/collect?$analyticsQueryParams"

  private val period = 10.seconds


  override def run(args: List[String]): IO[ExitCode] = {
    println(s"Bank URL: $bankUri")
    println(s"Analytics URL: $analyticsUri")
    println(s"Sending requests every $period")

    implicit val bankExchangeRateRW: ReadWriter[BankExchangeRate] = macroRW[BankExchangeRate]
    implicit val analyticsEventRW: ReadWriter[AnalyticsEvent] = macroRW[AnalyticsEvent]
    implicit val analyticsMessageRW: ReadWriter[AnalyticsMessage] = macroRW[AnalyticsMessage]

    val bankRequest = basicRequest.get(bankUri).response(asJson[Seq[BankExchangeRate]])

    def analyticsRequest(rate: Seq[Double]): Request[Either[String, String], Any] = {
      val message = AnalyticsMessage(
        client_id = "123456789.123456789",
        events = Seq(AnalyticsEvent(name = "usdtouah", params = rate.map("rate" -> _).toMap))
      )
      val jsonMessage = upickle.default.write(message)
      println("Sending JSON to Analytics:" + jsonMessage)

      basicRequest.post(analyticsUri).body(jsonMessage)
    }

    HttpClientFs2Backend.resource[IO]().use { backend =>
      val extractExchangeRate: IO[Seq[Double]] = bankRequest.send(backend).map { response =>
        response.body match {
          case Right(rates) =>
            println(s"Serialized response from bank: $rates")
            rates.map(_.rate)
          case Left(ex) =>
            ex.printStackTrace
            Seq.empty
        }
      }

      def sendToAnalytics(rate: Seq[Double]) =
        analyticsRequest(rate).send(backend)
          .map { response =>
            println("Analytics response body: " + response.body.getOrElse(""))
            println("Analytics response code: " + response.code)
          }

      fs2.Stream.awakeEvery(period)(Temporal[IO])
        .evalMap(_ => extractExchangeRate)
        .evalMap(sendToAnalytics)
        .compile
        .drain
    }.as(ExitCode.Success)

  }
}

case class BankExchangeRate(r030: Int, txt: String, rate: Double, cc: String, exchangedate: String)

case class AnalyticsMessage(client_id: String, events: Seq[AnalyticsEvent])
case class AnalyticsEvent(name: String, params: Map[String, Double])