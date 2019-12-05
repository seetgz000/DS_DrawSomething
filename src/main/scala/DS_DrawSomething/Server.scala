package DS_DrawSomething

import DS_DrawSomething.Client.{PlayerList, SomeoneLeft}
import DS_DrawSomething.Server.{AddReadyMember, Join, MemberList, ReadyMemberList, RemoveReadyMember}
import akka.actor.{Actor, ActorRef, DeadLetter}
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout

import scala.concurrent.duration._
import scalafx.application.Platform
import scalafx.collections.ObservableHashSet

import scala.concurrent.Await

class Server extends Actor{
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
      //when new member added, updates readymember list toos
      i.ref ! MemberList(memberList.toList)
      i.ref ! ReadyMemberList(readyMemberList.toList)
      //updates status of players
      i.ref ! PlayerList()
    }
  })

  readyMemberList.onChange({
    for (i<- memberList.toList){
      i.ref ! ReadyMemberList(readyMemberList.toList)
      //updates status of players
      i.ref ! PlayerList()
    }
  })

  def receive = {

    case DisassociatedEvent(local, remote, _) =>
      memberList.foreach(i =>{
        if(i.ref.path.address == remote){
          memberList -= i
          readyMemberList -= i
          memberList.foreach(_.ref ! SomeoneLeft(i.name))
        }
      })

      memberList.foreach(i =>{
        if(i.ref.path.address == remote){
          memberList.foreach(_.ref ! SomeoneLeft(i.name))
        }
      })





    case Join(name,ref) =>
      memberList += User(name,ref)
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

object Server {
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
