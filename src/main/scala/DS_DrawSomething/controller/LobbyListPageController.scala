package DS_DrawSomething.controller

import scalafx.scene.control.{Button, ComboBox, Spinner, TableView, TextField}
import scalafx.scene.control.TableView.TableViewSelectionModel
import scalafx.scene.layout.{Pane, StackPane}
import scalafxml.core.macros.sfxml

//later add, i add all UI essentials first
//might need to create objects for these controls to store
// private val tableLobby:TableView[Lobbies]
//private val dropDownTheme:ComboBox[Themes],
//private val spinnerPlayerNo:Spinner[],

@sfxml
class LobbyListPageController(private val btnCreateLobbyPopUp:Button,
                              private val btnRefresh:Button,
                              private val stackPanePopUpBackground:StackPane,
                              private val panePopUpBackground:Pane,
                              private val btnClosePopUp:Button,
                              private val txtLobbyBane:TextField,
                              private val btnCreateRoom:Button
                    )
{

  def openPopUp(): Unit ={
    stackPanePopUpBackground.toFront()
  }

  def closePopUp(): Unit ={
    stackPanePopUpBackground.toBack()
  }



}
