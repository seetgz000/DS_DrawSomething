package DS_DrawSomething.controller

import scalafx.scene.control.{Button, TextField}
import scalafxml.core.macros.sfxml
import DS_DrawSomething.Main
import scalafx.event.ActionEvent

@sfxml
class MainPageController(private val txtName:TextField,
                         private val btnPlay:Button) {

    def goToLobbyList():Unit ={
      Main.goToLobbyList()
    }

}
