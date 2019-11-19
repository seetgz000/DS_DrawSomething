package DS_DrawSomething.controller

import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, ProgressIndicator, TextArea, TextField}
import scalafx.scene.layout.{FlowPane, VBox}
import scalafxml.core.macros.sfxml

@sfxml
class GamePageController( //at top  of page, show current round of game and the word to be guessed by players
                          private val lblGameRounds:Label,
                          private val lblShowWord:Label,
                          // at the left side of page, include timer and (soon to be added toolbar for brushes)
                          private val piTimer:ProgressIndicator,
                          private val flowPanePaintToolbar:FlowPane,
                          //at tight side, show textbox components
                          private val txtGameChat:TextArea,
                          private val vBoxGameChat:VBox,
                          private val btnGameSubmitChat:Button,
                          //at bottom, show players names and scores,to be added later
                          private val vVoxPlayers:VBox,
                          //at center, show canvas to be drawn on and kick button
                          private val canvasPaint:Canvas,
                          private val btnKickPlayer: Button) {


}
