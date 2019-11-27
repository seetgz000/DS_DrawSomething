package DS_DrawSomething

import DS_DrawSomething.ChatClient.{Join, JoinMessage, ReceivedMessage, SendMessage}
import DS_DrawSomething.ChatServer.{Join, MemberList}
import akka.actor.{Actor, ActorRef, ActorSelection}
import scalafx.application.Platform
import akka.pattern.ask
import akka.util.Timeout
import scalafx.collections.ObservableHashSet
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._


class ChatClient extends Actor{
  implicit val timeout:Timeout = Timeout(10 second)//set implicit para for ? // set time until time out
  var serverOpt: Option[ActorSelection] = None
  //get updated list of clients and their names available through this
  var memberList = Iterable[User]()
  var nameList = Iterable[ActorRef]()

  def receive = {
    case "hello" => println("hello back at you")

      //join the server
    case ChatClient.Join(ip,port,name)=>
      serverOpt = Option(context.actorSelection(s"akka.tcp://chat@$ip:$port/user/server"))
      for(serverRef <- serverOpt){
        val answer = serverRef ? ChatServer.Join(name,context.self)
        answer.foreach(x => {
          if (x == "joined"){
            Platform.runLater({
              println("!,"+name+" have joined")
              context.become(joined)
              SendMessage(name," ")
              memberList.foreach(_.ref ! JoinMessage(name))
              println(context)
            })

          }
          else { //if cannot join , error message
            println("Cant join")
          }
        })
      }

    //updates the current users available in actor system
    case ChatServer.MemberList(x) =>
      memberList = x
      memberList.foreach(println(_))

    case ChatClient.SendMessage(name,msg) =>{
      memberList.foreach(_.ref ! ReceivedMessage(name,msg))
    }

    case _=> println("Failed to join")

  }

  //once joined a game
  def joined: Receive = {
    //server updates client how many clients currently in the system
    case ChatServer.MemberList(x) =>
      memberList = x
    //send text to all actors in list
    case ChatClient.SendMessage(name,msg) =>{
      memberList.foreach(_ .ref! ReceivedMessage(name,msg))
    }

    //when you received the message from actor
    case ReceivedMessage(name,msg)=>{
      Platform.runLater({
        Main.lobbyController.createChatBubbleClientAtLobby(name,msg)
      })
    }

    case JoinMessage(name)=>{
      Platform.runLater({
        Main.lobbyController.createJoinBubble(name)
      })
    }

    case _=>
  }


  }

object ChatClient{
  final case class Join(ip:String,port:String,name:String)
  final case class MemberList(list: Iterable[User])
  final case class SendMessage(name:String,message:String)
  final case class ReceivedMessage(name:String,message:String)
  final case class JoinMessage(name:String)
}
