package DS_DrawSomething.controller

import DS_DrawSomething.Main
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, TextArea}
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
                           private val btnSubmitChat:Button){
  //set spacing between chat bubbles
  vBoxChat.setSpacing(15)


  def goToLobbyList(): Unit ={
    Main.goToLobbyList()
  }

  def goToGamePage(): Unit ={
    Main.goToGamePage()
  }

  def createChatBubble(): Unit ={
    if (! txtChat.getText.isEmpty) {
<<<<<<< HEAD
      val borderHBox = new HBox()
=======
      val borderHBox = new HBox(){
        padding = Insets(5, 10, 5, 10)
      }
>>>>>>> b4024b388acd087aa637c3e24fb6b98a0303a46d
      borderHBox.maxWidth = 340

      val chatText = new Text(txtChat.getText)

      borderHBox.getChildren.add(chatText)
      chatText.wrappingWidthProperty.set(340)
      borderHBox.getStyleClass.add("chat-text")
      vBoxChat.getChildren.add(borderHBox)
    }
  }

  //use this to set text for label ater
  //lblLobbyName.setText("Lol")

}
