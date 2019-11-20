package DS_DrawSomething.controller

import DS_DrawSomething.Main
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, TextArea}
import scalafx.scene.layout.{FlowPane, VBox}
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

      val chatText = new Text(txtChat.getText)
      chatText.wrappingWidthProperty.set(375)
      chatText.getStyleClass.add("chat-text")
      vBoxChat.getChildren.add(chatText)
    }
  }

  //use this to set text for label ater
  //lblLobbyName.setText("Lol")

}
