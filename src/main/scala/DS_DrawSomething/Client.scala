package DS_DrawSomething

import akka.actor.{Actor, ActorSelection}
import akka.util.Timeout

import scala.concurrent.duration._

class Client extends Actor {
  var serverOpt: Option[ActorSelection] = None
  implicit val timeout: Timeout = Timeout(10 minute)


  def receive = {
    case _=>
  }
}
