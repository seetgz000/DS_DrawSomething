package DS_DrawSomething

import DS_DrawSomething.ChatServer.{Join, MemberList}
import akka.actor.{Actor, ActorRef}
import scalafx.application.Platform
import scalafx.collections.ObservableHashSet

class ChatServer extends Actor{

  var memberList = new ObservableHashSet[ActorRef]()
  var nameList = Iterable[String]()

  def receive = {
    case "hello" => println("hello at you")

    case ChatServer.Join(ref,name) =>
      memberList += ref
      nameList.mkString(name)
      println(memberList.size)
      sender() ! "joined" // tell sender "joined"
      println(s"${sender} have joined")
      memberList.foreach(_ ! MemberList(memberList.toList))

    case _       =>

  }

  def getMemberList:ObservableHashSet[ActorRef]= { memberList}


}

object ChatServer {
  final case class Join(ref:ActorRef,name:String)//to store Join msg itself
  final case class MemberList(list: Iterable[ActorRef])
  final case class NameList(list: Iterable[String])

}
