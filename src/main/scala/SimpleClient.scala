import scala.util.{Success, Failure}
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.event.Logging
import akka.io.IO
import spray.json._
import spray.can.Http
import spray.httpx.SprayJsonSupport
import spray.client.pipelining._
import spray.util._

// https://github.com/spray/spray/tree/master/examples

case class Elevation(location: Location, elevation: Double)

case class Location(lat: Double, lng: Double)

case class GoogleApiResult[T](status: String, results: List[T])

object ElevationJsonProtocol extends DefaultJsonProtocol {
  implicit val locationFormat = jsonFormat2(Location)
  implicit val elevationFormat = jsonFormat2(Elevation)

  implicit def googleApiResultFormat[T: JsonFormat] = jsonFormat2(GoogleApiResult.apply[T])
}

object SimpleClient {
  def main(arg: Array[String]) {
    // we need an ActorSystem to host our application in
    implicit val system = ActorSystem("simple-spray-client")
    import system.dispatcher
    // execution context for futures below
    val log = Logging(system, getClass)

    println("Requesting the elevation of Mt. Everest from Googles Elevation API...")

    import ElevationJsonProtocol._
    import SprayJsonSupport._
    val pipeline = sendReceive ~> unmarshal[GoogleApiResult[Elevation]]

    val responseFuture = pipeline {
      Get("http://maps.googleapis.com/maps/api/elevation/json?locations=27.988056,86.925278&sensor=false")
    }
    responseFuture onComplete {
      case Success(GoogleApiResult(_, Elevation(_, elevation) :: _)) =>
        println("The elevation of Mt. Everest is: {} m", elevation)
        shutdown()

      case Success(somethingUnexpected) =>
        println("The Google API call was successful but returned something unexpected: '{}'.", somethingUnexpected)
        shutdown()

      case Failure(error) =>
        println(error, "Couldn't get elevation")
        shutdown()
    }

    def shutdown(): Unit = {
      IO(Http).ask(Http.CloseAll)(1.second).await
      system.shutdown()
    }
  }
}