import akka.actor.ActorSystem
import spray.http._
import spray.client.pipelining._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

class requestThread(system: ActorSystem) extends Thread{

  override def run(): Unit = {
    implicit val s = system
    val url = "http://www.google.com/"
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val response = pipeline(Get(url))

    response onComplete {
      case Success(HttpResponse(code,entity,_,_)) => {
        if (code == StatusCodes.OK) println("it is ok.")
        println(code)
        println(entity)
      }
      case _ => println("Failed.")
    }
  }
}

object MyHttpRequest {
  def main(args: Array[String]) {
    implicit val system = ActorSystem()
    new requestThread(system).run()
    //val url = "http://www.google.com/"
    val url = "http://lodging-data-insights-web.us-east-1.test.expedia.com/"
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val response = pipeline(Post(url, "datelessPrice?hotelIds=5902864&tpid=3"))

    response onComplete {
      case Success(HttpResponse(code,entity,_,_)) => {
        if (code == StatusCodes.OK) println("it is ok.")
        println(code)
        println(entity)
      }
      case _ => println("Failed.")
    }

    Thread.sleep(10000)
    system.shutdown()

    }
}
