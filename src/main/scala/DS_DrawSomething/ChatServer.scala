package DS_DrawSomething

import DS_DrawSomething.ChatServer.{Join, MemberList}
import akka.actor.{Actor, ActorRef}
import scalafx.application.Platform
import scalafx.collections.ObservableHashSet

class ChatServer extends Actor{

  var memberList = new ObservableHashSet[User]()

  def receive = {
    case "hello" => println("hello at you")

    case ChatServer.Join(name,ref) =>

      memberList += User(name,ref)
      println(memberList.size)
      sender() ! "joined" // tell sender "joined"
      println(s"${sender} have joined")
      memberList.foreach(println(_))
      memberList.foreach(_.ref ! MemberList(memberList.toList))

    case _       =>

  }

  def getMemberList:ObservableHashSet[User]= { memberList}


}

object ChatServer {
  final case class Join(name:String,actorOf:ActorRef)//to store Join msg itself
  final case class MemberList(list: Iterable[User])
  final case class NameList(list: Iterable[String])

}
