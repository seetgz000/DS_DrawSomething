package DS_DrawSomething

import java.net.InetAddress

import DS_DrawSomething.ChatMain.{getClass, rootNode, stage, system}
import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.util.Duration
//testing chat
import java.net._


object Main extends JFXApp{
    /*
  testing chat
   */

    val localhost: InetAddress = InetAddress.getLocalHost
    val localIpAddress: String = localhost.getHostAddress

    var memberSize:Int = 0

    val overrideConf = ConfigFactory.parseString(
      s"""
         |akka {
         |  loglevel = "INFO"
         |
   |  actor {
         |    provider = "akka.remote.RemoteActorRefProvider"
         |  }
         |
   |  remote {
         |    enabled-transports = ["akka.remote.netty.tcp"]
         |    netty.tcp {
         |      hostname = "${localIpAddress}"
         |      port = 0
         |    }
         |
   |    log-sent-messages = on
         |    log-received-messages = on
         |  }
         |
   |}
         |
       """.stripMargin)

    val myConf = overrideConf.withFallback(ConfigFactory.load())
    val system = ActorSystem("chat", myConf)

    val serverRef = system.actorOf(Props[ChatServer](), "server" )
    val clientRef = system.actorOf(Props[ChatClient](), "client" )

    //1. testing, ok i got this shit down, next!
    //clientRef ! "hello"
    //serverRef ! "hello"
    //

    //2. joining... ok success can join now

    //3. test chat


  //bottom are for UI

  val rootResource = getClass.getResourceAsStream("view/MainPage.fxml")
  val loader = new FXMLLoader(null,NoDependencyResolver)
  loader.load(rootResource)

  val rootNode:scalafx.scene.layout.BorderPane = loader.getRoot[javafx.scene.layout.BorderPane]()
  val mainController = loader.getController[DS_DrawSomething.controller.MainPageController#Controller]()

  stage = new PrimaryStage() {
    scene = new Scene() {
      root = rootNode
      stylesheets add getClass.getResource("style/Style.css").toExternalForm
    }
  }

  //ensure it is always maximized
  stage.setMaximized(true)
  stage.setTitle("Draw something")


  //set global for all resource pages
  val lobbyListResource = getClass.getResourceAsStream("view/LobbyListPage.fxml")
  val lobbyListLoader = new FXMLLoader(null, NoDependencyResolver)
  lobbyListLoader.load(lobbyListResource)
  val lobbyListController = lobbyListLoader.getController[DS_DrawSomething.controller.LobbyListPageController#Controller]()

  val lobbyResource = getClass.getResourceAsStream("view/LobbyPage.fxml")
  val lobbyLoader = new FXMLLoader(null, NoDependencyResolver)
  lobbyLoader.load(lobbyResource)
  val lobbyController = lobbyLoader.getController[DS_DrawSomething.controller.LobbyPageController#Controller]()

  val gameResource = getClass.getResourceAsStream("view/GamePage.fxml")
  val gameLoader = new FXMLLoader(null, NoDependencyResolver)
  gameLoader.load(gameResource)
  val gamePageController = gameLoader.getController[DS_DrawSomething.controller.GamePageController#Controller]()


  //go to lobby list page
  def goToLobbyList(): Unit = {
    val rootNode:scalafx.scene.layout.StackPane = lobbyListLoader.getRoot[javafx.scene.layout.StackPane]()
    stage.scene().setRoot(rootNode)
  }


  //got back to main page
  //go to lobby list page
  def goToMainPage(): Unit = {
    val resource = getClass.getResourceAsStream("view/MainPage.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)

    loader.load(resource)

    val rootNode:scalafx.scene.layout.BorderPane = loader.getRoot[javafx.scene.layout.BorderPane]()
    stage.scene().setRoot(rootNode)
  }

  //got back to game page
  def goToGamePage(): Unit = {
    val rootNode:scalafx.scene.layout.BorderPane = gameLoader.getRoot[javafx.scene.layout.BorderPane]()
    stage.scene().setRoot(rootNode)
  }

  //go to lobby page
  def goToLobbyPage(): Unit = {
    val rootNode:scalafx.scene.layout.BorderPane = lobbyLoader.getRoot[javafx.scene.layout.BorderPane]()
    stage.scene().setRoot(rootNode)
  }

  stage.onCloseRequest = handle {
    system.terminate
  }

}
