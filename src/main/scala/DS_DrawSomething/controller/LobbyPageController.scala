package DS_DrawSomething.controller

import DS_DrawSomething.Client.{SendJoinMessage, SendMessage}
import DS_DrawSomething.Main.system
import DS_DrawSomething.{Main, User}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, ScrollPane, TextArea}
import scalafx.scene.layout.{FlowPane, HBox, VBox}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

@sfxml
class LobbyPageController (private val lblUserName:Label,
                            private val flowPanePlayers:FlowPane,
                           private val vBoxChat: VBox,
                           private val btnBackToLobbyList:Button,
                           private val btnStartGame:Button,
                           private val txtChat:TextArea,
                           private val btnSubmitChat:Button,
                           private val scrollPaneChat:ScrollPane) {


  var isReady = false

  //set spacing between chat bubbles
  vBoxChat.setSpacing(15)

  //set label user name at lobby page
  def setLabelName(name:String): Unit ={
    lblUserName.setText("Your name: "+name)
  }

  def goToMainPage(): Unit = {
    Main.goToMainPage()
  }

  //start game button
  def goToGamePage(): Unit = {
    if(!isReady) {
      Main.clientRef ! "ready"
      Main.clientRef ! "updateList"

      btnStartGame.text = "Waiting..."
      isReady = true
    }
    else{
      Main.clientRef ! "notReady"

      btnStartGame.text = "Start game"
      isReady = false

    }
  }

  // at chat box stuff
  //if member enter lobby
  def createJoinBubble(name: String): Unit = {
    //add new labels to flow panel
    val borderHBox = new HBox() {
      padding = Insets(5, 10, 5, 10)
    }
    borderHBox.maxWidth = 340

    val chatText = new Text(s"$name has joined the lobby.")

    borderHBox.getChildren.add(chatText)
    chatText.wrappingWidthProperty.set(340)
    borderHBox.getStyleClass.add("chat-green")
    vBoxChat.getChildren.add(borderHBox)
    //set to bottom
    scrollPaneChat.vvalueProperty.bind(vBoxChat.heightProperty)
  }

  //if member left the lobby
  def createQuitBubble(name: String): Unit = {
    //add new labels to flow panel
    val borderHBox = new HBox() {
      padding = Insets(5, 10, 5, 10)
    }
    borderHBox.maxWidth = 340

    val chatText = new Text(s"$name has left the lobby.")

    borderHBox.getChildren.add(chatText)
    chatText.wrappingWidthProperty.set(340)
    borderHBox.getStyleClass.add("chat-red")
    vBoxChat.getChildren.add(borderHBox)
    //set to bottom
    scrollPaneChat.vvalueProperty.bind(vBoxChat.heightProperty)
  }



  def createChatBubble(): Unit ={
    if (! txtChat.getText.isEmpty) {
      Main.clientRef ! SendMessage(Main.mainController.getUserName, txtChat.getText)
    }
    txtChat.text = ""

  }

  def createChatBubbleClientAtLobby(name:String,msg:String): Unit ={
    //add new labels to flow panel
      val borderHBox = new HBox(){
        padding = Insets(5, 10, 5, 10)
      }
      borderHBox.maxWidth = 340

      val chatText = new Text(s"${name}: ${msg}")

      borderHBox.getChildren.add(chatText)
      chatText.wrappingWidthProperty.set(340)
      borderHBox.getStyleClass.add("chat-text")
      vBoxChat.getChildren.add(borderHBox)
      //set to bottom
      scrollPaneChat.vvalueProperty.bind(vBoxChat.heightProperty)
  }

  //player list
  def generatePlayerList(userFromList:Iterable[User],readyUserList:Iterable[User]){
    flowPanePlayers.getChildren().clear()
    for(user<-userFromList) {        //add new labels to flow panel
      val borderHBox = new HBox() {
        padding = Insets(10, 10, 10, 10)
        margin = Insets(10, 10, 10, 10)
      }
      borderHBox.maxWidth = 340

      var chatText = new Text("")

      chatText = new Text(s"${user.name}    Status: preparing")
      borderHBox.getStyleClass.add("player-list-box")

      for (readyUser <- readyUserList) {
        if (user.ref.equals(readyUser.ref)) {
          chatText = new Text(s"${user.name}    Status: ready")
          borderHBox.getStyleClass.add("player-list-box-ready")
        }
      }//end for 1st

      borderHBox.getChildren.add(chatText)
      chatText.wrappingWidthProperty.set(340)
      flowPanePlayers.getChildren.add(borderHBox)
    }//end for 2nd
  }


}
