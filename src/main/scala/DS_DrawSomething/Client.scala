package DS_DrawSomething

import DS_DrawSomething.Client.{Join, JoinMessage, PlayerList, ReceivedDrawData, ReceivedEraseData, ReceivedMessage, SendDrawData, SendEraseData, SendJoinMessage, SendMessage, SomeoneLeft}
import DS_DrawSomething.Server.{MemberList, ReadyMemberList}
import akka.actor.{Actor, ActorRef, ActorSelection, DeadLetter}
import scalafx.application.Platform
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._


class Client extends Actor{
  implicit val timeout:Timeout = Timeout(10 second)//set implicit para for ? // set time until time out

  var serverOpt: Option[ActorSelection] = None
  //get updated list of clients and their names available through this
  var memberList = Iterable[User]()
  var readyMemberList = Iterable[User]()



  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
    context.system.eventStream.subscribe(self, classOf[DeadLetter])
  }

  def receive = {

    //join the server
    case Client.Join(ip,port,name)=>
      serverOpt = Option(context.actorSelection(s"akka.tcp://drawSomething@$ip:$port/user/server"))
      for(serverRef <- serverOpt){
        val answer = serverRef ? Server.Join(name,context.self)
        answer.foreach(x => {
          if (x.equals("joined")){
            Platform.runLater({
              Main.goToLobbyPage()
            })
            context.become(joined)
          }
        })
      }

    //updates the current users available in actor system
    case MemberList(x) =>
      memberList = x

    case ReadyMemberList(x) =>
      readyMemberList = x

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

    case PlayerList()=>
      Platform.runLater({
        Main.lobbyController.generatePlayerList(memberList,readyMemberList)
      })

    //if message cant be sent
    case DeadLetter(msg, from, to)=>
      Platform.runLater({
        Main.mainController.generateErrorMsg()
      })

    case _=>

  }


  //once joined a lobby
  def joined: Receive = {

    //when someone left the lobby
    case SomeoneLeft(name) =>
      Platform.runLater({
        Main.lobbyController.createQuitBubble(name)
      })

    //server updates client how many clients currently in the system
    case MemberList(x) =>
      memberList = x
    //      //updates status of players
    //      memberList.foreach(_.ref ! PlayerList())

    case ReadyMemberList(x) =>
      readyMemberList = x
    //      //updates status of players
    //      memberList.foreach(_.ref ! PlayerList())

    //send text to all actors in list
    case Client.SendMessage(name,msg) =>{
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

    //updates the player list
    case "updateList" =>{
      memberList.foreach(_.ref ! PlayerList())
    }

    //button click tells client its ready, which then tell server you are ready
    case "ready" =>
      for(serverRef <- serverOpt) {
        serverRef ! DS_DrawSomething.Server.AddReadyMember(Main.mainController.getUserName, context.self)
      }

    //button click tells clients its not ready, which also then notify server
    case "notReady" =>
      for(serverRef <- serverOpt) {
        serverRef ! DS_DrawSomething.Server.RemoveReadyMember(Main.mainController.getUserName, context.self)
      }

    //go to game page
    case "start" =>
      memberList.foreach(_.ref ! PlayerList())
      Platform.runLater({
        Main.goToGamePage()
        Main.gamePageController.getTimer.play()
        Main.gamePageController.generateGamePlayerList(memberList,readyMemberList)
      })
      context.become(start)

      println("server is active")
      Main.serverRef ! "connecting"
    case "connecting" =>


    //tell clients to quit the game
    case "end" =>
      Platform.runLater({
        Main.mainController.endGame()
      })

    //if message cant be sent
    case DeadLetter(msg, from, to)=>
      Platform.runLater({
        Main.mainController.endGame()
      })

    case _=>

  }

  //when go to game page and start playing the game
  def start:Receive = {
    //send text to all actors in list
    case Client.SendMessage(name,msg) =>{
      memberList.foreach(_ .ref ! ReceivedMessage(name,msg))
    }

    //when you received the message from actor
    case ReceivedMessage(name,msg)=>{
      Platform.runLater({
        Main.gamePageController.createChatBubbleClientAtGame(name,msg)
      })
    }

    //send paint data to all actors in list
    case Client.SendDrawData(x, y, w, h, aw, ah) =>{
      memberList.foreach(_ .ref ! ReceivedDrawData(x, y, w, h, aw, ah))
    }

    //when you received the message from actor
    case ReceivedDrawData(x, y, w, h, aw, ah)=>{
      Platform.runLater({
        Main.gamePageController.updateCanvas(x, y, w, h, aw, ah)
      })
    }

    //send erase data to all actors in list
    case Client.SendEraseData(x, y, w, h) =>{
      memberList.foreach(_ .ref ! ReceivedEraseData(x, y, w, h))
    }

    //when you received the message from actor
    case ReceivedEraseData(x, y, w, h)=>{
      Platform.runLater({
        Main.gamePageController.updateCanvas(x, y, w, h)
      })
    }

    case _=>
  }


}

object Client{
  final case class Join(ip:String,port:String,name:String)
  //if someone left the lobby
  final case class SomeoneLeft(name:String)
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
  final case class SendDrawData(penX: Double, penY: Double, penW: Double, penH: Double, penArcWidth: Double, penArcHeight: Double)
  final case class SendEraseData(eraserX: Double, eraserY: Double, eraserW: Double, eraserH: Double)
  final case class ReceivedDrawData(penX: Double, penY: Double, penW: Double, penH: Double, penArcWidth: Double, penArcHeight: Double)
  final case class ReceivedEraseData(eraserX: Double, eraserY: Double, eraserW: Double, eraserH: Double)
  //
}
