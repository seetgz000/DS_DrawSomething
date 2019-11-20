package DS_DrawSomething.controller

import javafx.event
import javafx.event.EventHandler
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, ColorPicker, Label, ProgressIndicator, Slider, TextArea, TextField, ToggleButton, ToggleGroup}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{FlowPane, VBox}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Cursor

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
                          private val btnKickPlayer: Button,
                          private val btnPen: Button,
                          private val btnEraser: Button,
                          private val colorPicker: ColorPicker,
                          private val sliderSize: Slider) {


  val gc = canvasPaint.graphicsContext2D
  val penSize = 5
  val penCoordinate = penSize / 2
  var paintTool = "pen"
  gc.fill = Color.Black


  btnPen.setCursor(Cursor.Hand)
  btnEraser.setCursor(Cursor.Hand)
  btnKickPlayer.setCursor(Cursor.Hand)
  btnGameSubmitChat.setCursor(Cursor.Hand)

  btnPen.onMouseClicked() = (e: MouseEvent) => {
    paintTool = "pen"
  }
  btnEraser.onMouseClicked() = (e: MouseEvent) => {
    paintTool = "eraser"
  }

  colorPicker.onAction = (e: ActionEvent) => {
    gc.fill = colorPicker.getValue()
  }

  canvasPaint.onMouseDragged() = (e: MouseEvent) => {
    if (paintTool == "pen"){
      drawCanvas(e)
    } else if (paintTool == "eraser") {
      eraser(e)
    }
  }

  // Fill the Canvas with a Blue rectangle when the user double-clicks
  canvasPaint.onMouseClicked() = (e: MouseEvent) => {
    eraserAndReset(e)
  }




  // Draw line as the user drags the mouse
  def drawCanvas(e: MouseEvent): Unit = {
    gc.fillRoundRect(e.x - penCoordinate, e.y - penCoordinate, penSize, penSize, penSize, penSize)
  }

  // Clear away portions as the user drags the mouse
  def eraser(e: MouseEvent): Unit = {
    gc.clearRect(e.x - penCoordinate, e.y - penCoordinate, penSize, penSize)
  }

  // Fill the Canvas with a Blue rectangle when the user double-clicks
  def eraserAndReset(e: MouseEvent): Unit = {
    if (e.clickCount > 1) {
      resetCanvas(Color.Blue)
    } else {
      gc.fillRoundRect(e.x - penCoordinate, e.y - penCoordinate, penSize, penSize, penSize, penSize)
    }
  }

  def reset: Unit = {
      resetCanvas(Color.Red)
  }

  /**
   * Resets the canvas to its original look by filling in a rectangle covering
   * its entire width and height. Color.Blue is used in this demo.
   *
   * @param color The color to fill
   */
  private def resetCanvas(color: Color): Unit = {
    gc.fill = color
    gc.fillRect(0, 0, canvasPaint.width.get, canvasPaint.height.get)
  }

}
