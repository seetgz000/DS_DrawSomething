package DS_DrawSomething

import DS_DrawSomething.ChatClient.{Join, JoinMessage, PlayerList, ReceivedMessage, SendJoinMessage, SendMessage, SetReady}
import DS_DrawSomething.ChatServer.{MemberList,ReadyMemberList}
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
  var readyMemberList = Iterable[User]()


  var isReady = false


  def receive = {

      //join the server
    case ChatClient.Join(ip,port,name)=>
      serverOpt = Option(context.actorSelection(s"akka.tcp://chat@$ip:$port/user/server"))
      for(serverRef <- serverOpt){
        val answer = serverRef ? ChatServer.Join(name,context.self)
        answer.foreach(x => {
          if (x.equals("joined")){
            Platform.runLater({
              Main.goToLobbyPage()
            })
            context.become(joined)
          }
          else { //if cannot join , error message
            println("Cant join")
            Platform.runLater({
              Main.mainController.generateErrorMsg()
            })
          }
        })
      }

//    //updates the current users available in actor system
    case MemberList(x) =>
        memberList = x

    case SendMessage(name,msg) =>{
      memberList.foreach(_.ref ! ReceivedMessage(name,msg))
    }

    case SendJoinMessage(name) =>{
      memberList.foreach(_.ref ! JoinMessage(name))
    }


    //when you received the message from actor
    case ReceivedMessage(name,msg)=>{
      Platform.runLater({
        Main.lobbyController.createChatBubbleClientAtLobby(name,msg)
      })
    }

    case PlayerList()=>{
      Platform.runLater({
          Main.lobbyController.generatePlayerList(memberList,readyMemberList)
      })
    }

    case _=>

  }


  //once joined a lobby
  def joined: Receive = {
    //server updates client how many clients currently in the system
    case MemberList(x) =>
      memberList = x

    case ReadyMemberList(x) =>
      println(x.size + " ready")
      readyMemberList = x
      //updates status of players
      memberList.foreach(_.ref ! PlayerList())

    //send text to all actors in list
    case ChatClient.SendMessage(name,msg) =>{
      memberList.foreach(_ .ref ! ReceivedMessage(name,msg))
    }

    //when you received the message from actor
    case ReceivedMessage(name,msg)=>{
      Platform.runLater({
        Main.lobbyController.createChatBubbleClientAtLobby(name,msg)
      })
    }

    //tell all players in lobby that you have joined
    case SendJoinMessage(name) =>{
      memberList.foreach(_.ref ! JoinMessage(name))
    }

    case JoinMessage(name)=>{
      Platform.runLater({
        Main.lobbyController.createJoinBubble(name)
      })
    }

    case PlayerList()=>{
      Platform.runLater({
        Main.lobbyController.generatePlayerList(memberList,readyMemberList)
      })
    }

    case "updateList" =>{
      memberList.foreach(_.ref ! PlayerList())
    }

    //button click tells client its ready, which then tell server you are ready
    case "ready" =>
      for(serverRef <- serverOpt) {
        serverRef ! ChatServer.AddReadyMember(Main.mainController.getUserName, context.self)
      }

    //button click tells clients its not ready, which also then notify server
    case "notReady" =>
      for(serverRef <- serverOpt) {
        serverRef ! ChatServer.RemoveReadyMember(Main.mainController.getUserName, context.self)
      }

    //go to game page
    case "start" =>
      memberList.foreach(_.ref ! PlayerList())
      Platform.runLater({
        Main.goToGamePage()
        Main.gamePageController.getTimer.play()
      })
      context.become(start)


    case _=>

  }

  def start:Receive = {
    //send text to all actors in list
    case ChatClient.SendMessage(name,msg) =>{
      memberList.foreach(_ .ref ! ReceivedMessage(name,msg))
    }

    //when you received the message from actor
    case ReceivedMessage(name,msg)=>{
      Platform.runLater({
        Main.gamePageController.createChatBubbleClientAtGame(name,msg)
      })
    }

    case _=>
  }


  }

object ChatClient{
  final case class Join(ip:String,port:String,name:String)
  //to update own iterables
  final case class MemberList(list: Iterable[User])
  final case class ReadyMemberList(list:Iterable[User])
  //send messgae at lobby
  final case class SendMessage(name:String,message:String)
  final case class ReceivedMessage(name:String,message:String)
  //tell everyone you have joined
  final case class SendJoinMessage(name:String)
  final case class JoinMessage(name:String)
  final case class PlayerList()
  //
  final case class SetReady()
}
