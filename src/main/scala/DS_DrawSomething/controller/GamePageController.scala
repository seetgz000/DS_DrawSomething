package DS_DrawSomething.controller

import javafx.beans.value.ObservableValue
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, ColorPicker, Label, ProgressIndicator, Slider, TextArea, TextField, ToggleButton, ToggleGroup}
import scalafx.scene.input.{DragEvent, MouseEvent, ScrollEvent}
import scalafx.scene.layout.{AnchorPane, BorderPane, FlowPane, VBox}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.event.ActionEvent
import scalafx.scene.Cursor
import scalafx.util.Duration

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
                          private val sliderToolSize: Slider,
                          private val lblToolSize: Label,
                          private val lblTimer:Label,
                          private val borderPaneResult:BorderPane,
                          private val lblWinner:Label,
                          private val lblSecondPlace:Label,
                          private val lblThirdPlace:Label,
                          private val btnNextRound:Button) {

  //set visibility of nodes
  borderPaneResult.setVisible(false)

  //start the game
  var timerCounter:Double = 0
  val timer = new PauseTransition(Duration(1000))
  timer.onFinished = {_ =>
    timerCounter += 0.02
    lblTimer.text = (50 - timerCounter*50).toInt.toString
    piTimer.setProgress(timerCounter)

    lblTimer.toFront()

    if (timerCounter <= 1){
      timer.playFromStart() // Wait another second, or you can opt to finish instead.
    }
    else{
      borderPaneResult.setVisible(true)
      lblTimer.toBack()
    }
  }


  val gc = canvasPaint.graphicsContext2D
  sliderToolSize.value = 5 //set default tool size to 5
  var toolSize = sliderToolSize.value.toInt
  lblToolSize.text = toolSize.toString
  var penSize = toolSize
  var penCoordinate = penSize / 2
  var eraserSize = toolSize
  var eraserCoordinate = eraserSize / 2
  var paintTool = "pen"
  colorPicker.value = Color.Black
  gc.fill = Color.Black


  btnPen.setCursor(Cursor.Hand)
  btnEraser.setCursor(Cursor.Hand)
  btnKickPlayer.setCursor(Cursor.Hand)
  btnGameSubmitChat.setCursor(Cursor.Hand)

  sliderToolSize.valueProperty.addListener{ (o: javafx.beans.value.ObservableValue[_ <: Number], oldVal: Number, newVal: Number) =>
    changeToolSize(sliderToolSize.value.toInt, paintTool)
  }

  //When clicked change to pen tool
  btnPen.onMouseClicked() = (e: MouseEvent) => {
    paintTool = "pen"
    sliderToolSize.value = penSize
    changeToolSize(sliderToolSize.value.toInt, paintTool)
  }
  //When clicked change to eraser tool
  btnEraser.onMouseClicked() = (e: MouseEvent) => {
    paintTool = "eraser"
    sliderToolSize.value = eraserSize
    changeToolSize(sliderToolSize.value.toInt, paintTool)
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
    if (paintTool == "pen"){
      drawCanvas(e)
    } else if (paintTool == "eraser") {
      eraser(e)
    }
  }

  def goToNextRound(e:MouseEvent): Unit ={
    timerCounter = 0
    borderPaneResult.setVisible(false)
    eraseEverything(e)
    timer.play()
  }

  def eraseEverything(e: MouseEvent): Unit = {
    gc.clearRect(0, 0, canvasPaint.getWidth, canvasPaint.getHeight)
  }

  // Draw line as the user drags the mouse
  def drawCanvas(e: MouseEvent): Unit = {
    gc.fillRoundRect(e.x - penCoordinate, e.y - penCoordinate, penSize, penSize, penSize, penSize)
  }

  // Clear away portions as the user drags the mouse
  def eraser(e: MouseEvent): Unit = {
    gc.clearRect(e.x - eraserCoordinate, e.y - eraserCoordinate, eraserSize, eraserSize)
  }

  // Fill the Canvas with a White rectangle for reset
  def reset: Unit = {
      resetCanvas(Color.White)
  }

  /**
   * Resets the canvas to its original look by filling in a rectangle covering
   *
   * @param color The color to fill
   */
  private def resetCanvas(color: Color): Unit = {
    gc.fill = color
    gc.fillRect(0, 0, canvasPaint.width.get, canvasPaint.height.get)
  }

  def changeToolSize(size: Int, paintTool: String): Unit = {
    toolSize = size
    lblToolSize.text = toolSize.toString
    if (paintTool == "pen") {
      penSize = toolSize
      penCoordinate = penSize / 2
    } else if (paintTool == "eraser"){
      eraserSize = toolSize
      eraserCoordinate = eraserSize / 2
    }
  }

  //getter and setter
  def getTimer:PauseTransition ={
    timer
  }


}
