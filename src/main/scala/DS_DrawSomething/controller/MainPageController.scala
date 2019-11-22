package DS_DrawSomething.controller

import scalafx.scene.control.{Button, TextField}
import scalafxml.core.macros.sfxml
import DS_DrawSomething.Main
import scalafx.event.ActionEvent

@sfxml
class MainPageController(private val txtName:TextField,
                         private val btnPlay:Button) {


    var userName:String = ""

    def goToLobbyList():Unit ={
        userName = txtName.text.value
        Main.goToLobbyList()

    }

    //getter for name
    def getUserName:String = {
      userName
    }

}
