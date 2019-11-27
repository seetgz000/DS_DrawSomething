package DS_DrawSomething

import akka.actor.{Actor, ActorRef}
import akka.remote.DisassociatedEvent
import akka.util.Timeout
import scalafx.collections.ObservableHashSet

import scala.concurrent.Await
import scala.concurrent.duration._


//class Server extends Actor {
//  implicit val timeout = Timeout(10 second)
//  val members = new ObservableHashSet[ActorRef]()
//  members.onChange({
//    for (member <- members.toList){
//      member ! MemberList(members.toList)
//    }
//  })
//
//
//  def receive ={
//    case DisassociatedEvent(local, remote, _) =>
//      members.removeIf(x => x.path.address == remote)
//    case "start" =>
//
//    case _ =>
//  }
//
//
//  def started: Receive = {
//    case DisassociatedEvent(local, remote, _) =>
//      for (member <- members){
//        member ! //close game
//      }
//
//    case _ =>
//  }
//}

object Server {

}
