package DS_DrawSomething

import DS_DrawSomething.ChatServer.{AddReadyMember, Join, MemberList, ReadyMemberList, RemoveReadyMember}
import akka.actor.{Actor, ActorRef, DeadLetter}
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout

import scala.concurrent.duration._
import scalafx.application.Platform
import scalafx.collections.ObservableHashSet

import scala.concurrent.Await

class ChatServer extends Actor{
  implicit val time = Timeout(10 second)// set time until time out

  var memberList = new ObservableHashSet[User]()
  var readyMemberList = new ObservableHashSet[User]()

  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
    context.system.eventStream.subscribe(self, classOf[akka.remote.AssociatedEvent])
    context.system.eventStream.subscribe(Main.serverRef, classOf[DeadLetter])
  }

  //updates lists at client's
  memberList.onChange({
    for (i <- memberList.toList){
      i.ref ! MemberList(memberList.toList)
      //when new member added, updates readymember list too
    }
  })

  readyMemberList.onChange({
    for (i<- memberList.toList){
      i.ref ! ReadyMemberList(readyMemberList.toList)
    }
  })

  def receive = {
    case DisassociatedEvent(local, remote, _) =>
      memberList.removeIf(x => {
        x.ref.path.address == remote
      })

    case Join(name,ref) =>
      memberList += User(name,ref)
      println(memberList.size)
      sender ! "joined" // tell sender "joined"
      println(s"$sender have joined")


    //tell server its ready
    case AddReadyMember(name,ref) =>
      readyMemberList += User(name,ref)
      println(readyMemberList.size + " "+ name)
      //if players that are ready matches total members
      if (readyMemberList.size == memberList.size){
        memberList.foreach(_.ref ! "start")
      }

    case RemoveReadyMember(name,ref) =>
      readyMemberList.foreach(i=>{
        if (i.ref.equals(ref)){
          readyMemberList-=i
        }
      })


    case _=>

  }

  def getMemberList:ObservableHashSet[User]= { memberList}


}

object ChatServer {
  final case class Join(name:String,actorOf:ActorRef)//to store Join msg itself
  final case class MemberList(list: Iterable[User])
  final case class ReadyMemberList(list:Iterable[User])
  final case class NameList(list: Iterable[String])
  //for player list
  final case class PlayerList(list:Iterable[User])
  //receive added or removed ready members
  final case class AddReadyMember(name:String,ref:ActorRef)
  final case class RemoveReadyMember(name:String,ref:ActorRef)



}
