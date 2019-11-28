package DS_DrawSomething.controller

import DS_DrawSomething.ChatClient.{SendJoinMessage, SendMessage, SetReady}
import DS_DrawSomething.{Main, User}
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, ScrollPane, TextArea}
import scalafx.scene.layout.{FlowPane, HBox, VBox}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class LobbyPageController (private val lblLobbyName:Label,
                            private val flowPanePlayers:FlowPane,
                           private val vBoxChat: VBox,
                           private val btnBackToLobbyList:Button,
                           private val btnStartGame:Button,
                           private val txtChat:TextArea,
                           private val btnSubmitChat:Button,
                           private val scrollPaneChat:ScrollPane) {
  //set spacing between chat bubbles
  vBoxChat.setSpacing(15)


  def goToMainPage(): Unit = {
    Main.goToMainPage()

  }

  def goToGamePage(): Unit = {
    Main.clientRef ! "ready"
    Main.clientRef !  "updateList"
  }

  //later implement status bar to game page there and disocciation event


  // at chat box stuff
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


  def createChatBubble(): Unit ={
    if (! txtChat.getText.isEmpty) {
      Main.clientRef ! SendMessage(Main.mainController.getUserName, txtChat.getText)
    }

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

      println("not clicked")

      for (readyUser <- readyUserList) {
        if (user.name.equals(readyUser.name)) {
          chatText = new Text(s"${user.name}    Status: ready")
          borderHBox.getStyleClass.add("player-list-box-ready")
          println("clicked")
        }
      }//end for 1st

      borderHBox.getChildren.add(chatText)
      chatText.wrappingWidthProperty.set(340)
      flowPanePlayers.getChildren.add(borderHBox)
    }//end for 2nd
  }

}
