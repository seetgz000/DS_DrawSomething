package DS_DrawSomething

import DS_DrawSomething.ChatClient.{Join, ReceivedMessage, SendMessage}
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
  var memberList = Iterable[ActorRef]()
  var nameList = Iterable[ActorRef]()

  def receive = {
    case "hello" => println("hello back at you")

      //join the server
    case ChatClient.Join(ip,name)=>
      serverOpt = Option(context.actorSelection(s"akka.tcp://chat@$ip:${name}/user/server"))
      for(serverRef <- serverOpt){
        val answer = serverRef ? ChatServer.Join(context.self,name)
        answer.foreach(x => {
          if (x == "joined"){
            Platform.runLater({
              println(name+" joined")
            })
            context.become(joined)
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
      memberList.foreach(_ ! ReceivedMessage(name,msg))
    }

    case _=> println("Failed to join")

  }

  //once joined a game
  def joined: Receive = {
    case ChatServer.MemberList(x) =>
      memberList = x
      memberList.foreach(println(_))

    //send text to all actors in list
    case ChatClient.SendMessage(name,msg) =>{
      memberList.foreach(_ ! ReceivedMessage(name,msg))
    }

    //when you received the message from actor
    case ReceivedMessage(name,msg)=>{
      Platform.runLater({
        println("Can send")
        Main.lobbyController.createChatBubbleClientAtLobby(name,msg)
      })
    }

    case _=>
  }


  }

object ChatClient{
  final case class Join(ip:String,name:String)
  final case class MemberList(list: Iterable[ActorRef])
  final case class SendMessage(name:String,message:String)
  final case class ReceivedMessage(name:String,message:String)
}
