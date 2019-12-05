package DS_DrawSomething.controller

import java.net.InetAddress

import DS_DrawSomething.Client.{Join, SendJoinMessage}
import scalafx.scene.control.{Alert, Button, Label, TextField}
import scalafxml.core.macros.sfxml
import DS_DrawSomething.Main
import DS_DrawSomething.Main.system
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

@sfxml
class MainPageController(private val txtName:TextField,
                         private val txtIpAddress:TextField,
                         private val txtPort:TextField,
                         private val btnPlay:Button) {


    var userName:String = ""

    def goToLobbyList():Unit ={
            if (!txtName.text.value.isEmpty &&
              ! txtPort.text.value.isEmpty &&
              txtIpAddress.text.value.matches("^([0-9.]+)$")) {
                Main.clientRef ! Join(txtIpAddress.text.value, txtPort.text.value,txtName.text.value)
                userName = txtName.text.value
            }
            else if(txtName.text.value.isEmpty){
                new Alert(AlertType.Information, "Invalid name, please try again").showAndWait()
            }
            else if(txtPort.text.value.isEmpty || !txtPort.text.value.matches("^([0-9.]+)$")){
                new Alert(AlertType.Information, "Invalid port, please try again").showAndWait()
            }
            else if(txtIpAddress.text.value.isEmpty || !txtIpAddress.text.value.matches("^([0-9.]+)$"))
                new Alert(AlertType.Information, "Invalid ip address, please try again").showAndWait()

    }

    //getter for name
    def getUserName:String = {
      userName
    }

    def generateErrorMsg(): Unit ={
        new Alert(AlertType.Information, "Invalid port or ip address, please try again").showAndWait()
    }


    //end the whole game
    def endGame(): Unit ={
      new Alert(AlertType.Information, "Error 404. Server is not available to process request.").showAndWait()
      system.terminate
      Platform.exit()
    }

}
