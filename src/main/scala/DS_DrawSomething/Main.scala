package DS_DrawSomething


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

  stage = new PrimaryStage() {
    scene = new Scene() {
      root = rootNode
      stylesheets add getClass.getResource("style/Style.css").toExternalForm
    }
  }
  //stage.setMaximized(true)



  }
