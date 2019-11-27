package DS_DrawSomething.controller

import java.net.InetAddress

import DS_DrawSomething.ChatClient.Join
import scalafx.scene.control.{Alert, Button, TextField}
import scalafxml.core.macros.sfxml
import DS_DrawSomething.Main
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

@sfxml
class MainPageController(private val txtName:TextField,
                         private val txtIpAddress:TextField,
                         private val btnPlay:Button) {


    var userName:String = ""

    def goToLobbyList():Unit ={
            if (!txtName.text.value.equals("") &&
              txtIpAddress.text.value.matches("^([0-9.]+)$") && InetAddress.getByName(txtIpAddress.text.value).isReachable(500)) {
                Main.clientRef ! Join(txtIpAddress.text.value, txtName.text.value)
                userName = txtName.text.value
                Main.goToLobbyPage()
            }
            else if(txtName.text.value.isEmpty){
                new Alert(AlertType.Information, "Invalid name, please try again").showAndWait()
            }
            else
                new Alert(AlertType.Information, "Invalid ip address, please try again").showAndWait()

    }

    //getter for name
    def getUserName:String = {
      userName
    }

}
