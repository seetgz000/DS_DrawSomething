package DS_DrawSomething.controller

import DS_DrawSomething.ChatClient.SendMessage
import DS_DrawSomething.Main
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
                           private val scrollPaneChat:ScrollPane){
  //set spacing between chat bubbles
  vBoxChat.setSpacing(15)


  def goToLobbyList(): Unit ={
    Main.goToLobbyList()
  }

  def goToGamePage(): Unit ={
    Main.goToGamePage()
    //start timer at game page
    Main.gamePageController.getTimer.play()
  }

  def createChatBubble(): Unit ={
    Main.clientRef ! SendMessage(Main.mainController.getUserName,txtChat.getText)

  }

  def createChatBubbleClientAtLobby(name:String,msg:String): Unit ={
    //add new labels to flow panel
    if (! txtChat.getText.isEmpty) {
      val borderHBox = new HBox(){
        padding = Insets(5, 10, 5, 10)
      }
      borderHBox.maxWidth = 340

      val chatText = new Text(s"${name}: ${msg}")

      borderHBox.getChildren.add(chatText)
      chatText.wrappingWidthProperty.set(340)
      borderHBox.getStyleClass.add("chat-text")
      vBoxChat.getChildren.add(borderHBox)
    }
  }

  //use this to set text for label ater
  //lblLobbyName.setText("Lol")

}
