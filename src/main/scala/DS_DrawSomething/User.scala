package DS_DrawSomething

import akka.actor.ActorRef

case class User(name: String, ref: ActorRef) {
  override def toString: String = {
    name
  }
}