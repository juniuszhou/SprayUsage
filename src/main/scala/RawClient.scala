import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import akka.io.IO

import spray.can.Http
import spray.http._
import HttpMethods._

import scala.util.Success

// case class GoogleApiResult[T](status: String, results: List[T])

object RawClient {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("simple-spray-client")
    implicit val timeout = Timeout(15.seconds)
    val url = "http://www.github.com"
    import system.dispatcher
    // implicit execution context
    val response = (IO(Http) ? HttpRequest(GET, Uri(url))).mapTo[HttpResponse]

    response onComplete {
      case Success(HttpResponse(_,_,_,_)) => println()
      case _ => println("Failed.")
    }

    IO(Http).ask(Http.CloseAll)(1.second)
    system.shutdown()

    /*
    val response2 = (IO(Http) ? Get(url)).mapTo[HttpResponse]

    response2 onComplete {
      case Success(_) => println()
      case _ => println("Failed.")
    }
  */
  }
}
