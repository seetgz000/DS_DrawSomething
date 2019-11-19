package DS_DrawSomething.controller

import DS_DrawSomething.Main
import scalafx.scene.control.{Button, Label, TextArea}
import scalafx.scene.layout.{FlowPane, VBox}
import scalafxml.core.macros.sfxml

@sfxml
class LobbyPageController (private val lblLobbyName:Label,
                            private val flowPanePlayers:FlowPane,
                           private val vBoxChat: VBox,
                           private val btnBackToLobbyList:Button,
                           private val btnStartGame:Button,
                           private val txtChat:TextArea,
                           private val btnSubmitChat:Button){

  def goToLobbyList(): Unit ={
    Main.goToLobbyList()
  }

  def goToGamePage(): Unit ={
    Main.goToGamePage()
  }

  lblLobbyName.setText("Lol")

}
