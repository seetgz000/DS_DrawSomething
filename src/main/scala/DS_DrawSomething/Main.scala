package DS_DrawSomething

import scala.collection.JavaConverters._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._

object Main extends JFXApp{

  val rootResource = getClass.getResourceAsStream("view/MainPage.fxml")
  val loader = new FXMLLoader(null,NoDependencyResolver)
  loader.load(rootResource)

  val rootNode:scalafx.scene.layout.BorderPane = loader.getRoot[javafx.scene.layout.BorderPane]()
  val controller = loader.getController[DS_DrawSomething.controller.MainPageController#Controller]()

  stage = new PrimaryStage() {
    scene = new Scene() {
      root = rootNode
      stylesheets add getClass.getResource("style/Style.css").toExternalForm
    }
  }

  //ensure it is always maximized
  stage.setMaximized(true)


  //go to lobby list page
  def goToLobbyList(): Unit = {
    val resource = getClass.getResourceAsStream("view/LobbyListPage.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)

    loader.load(resource)

    val rootNode:scalafx.scene.layout.StackPane = loader.getRoot[javafx.scene.layout.StackPane]()
    stage.scene().setRoot(rootNode)
  }




}
