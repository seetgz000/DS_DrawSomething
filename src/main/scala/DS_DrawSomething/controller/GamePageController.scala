package DS_DrawSomething.controller

import DS_DrawSomething.Client.SendMessage
import DS_DrawSomething.{Main, User}
import javafx.beans.value.ObservableValue
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, ColorPicker, Label, ProgressIndicator, ScrollPane, Slider, TextArea, TextField, ToggleButton, ToggleGroup}
import scalafx.scene.input.{DragEvent, MouseEvent, ScrollEvent}
import scalafx.scene.layout.{AnchorPane, BorderPane, FlowPane, HBox, VBox}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Cursor
import scalafx.scene.text.Text
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
                          private val scrollPaneGameChat:ScrollPane,
                          //at bottom, show players names and scores,to be added later
                          private val vVoxPlayers:VBox,
                          //at center, show canvas to be drawn on and kick button
                          private val canvasPaint:Canvas,
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
                          private val btnNextRound:Button,
                          private val hBoxPlayers: HBox) {

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

  var role = "player"

  btnPen.setCursor(Cursor.Hand)
  btnEraser.setCursor(Cursor.Hand)
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
//  var penX: Double = 0
//  var penY: Double = 0
//  var penW: Double = 0
//  var penH: Double = 0
//  var penArcWidth: Double = 0
//  var penArcHeight: Double = 0
//  var eraserX: Double = 0
//  var eraserY: Double = 0
//  var eraserW: Double = 0
//  var eraserH: Double = 0

  def updateCanvas(penX: Double, penY: Double, penW: Double, penH: Double, penArcWidth: Double, penArcHeight: Double): Unit ={
    gc.fillRoundRect(penX, penY, penW, penH, penArcWidth, penArcHeight)
  }

  def updateCanvas(eraserX: Double, eraserY: Double, eraserW: Double, eraserH: Double): Unit ={
    gc.clearRect(eraserX, eraserY, eraserW, eraserH)
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
//    penX = e.x - penCoordinate
//    penY = e.y - penCoordinate
//    penW = penSize
//    penH = penSize
//    penArcWidth = penSize
//    penArcHeight = penSize
    updateCanvas(e.x - penCoordinate, e.y - penCoordinate, penSize, penSize, penSize, penSize)
  }

  // Clear away portions as the user drags the mouse
  def eraser(e: MouseEvent): Unit = {
    gc.clearRect(e.x - eraserCoordinate, e.y - eraserCoordinate, eraserSize, eraserSize)
//    eraserX = e.x - eraserCoordinate
//    eraserY = e.y - eraserCoordinate
//    eraserW = eraserSize
//    eraserH = eraserSize
    updateCanvas(e.x - eraserCoordinate, e.y - eraserCoordinate, eraserSize, eraserSize)
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

  //transitioning to lobby page
  def goToLobbyPage(): Unit ={
    Main.goToLobbyPage()
  }

  //getter and setter
  def getTimer:PauseTransition ={
    timer
  }

  //chat box methods
  def createChatBubble(): Unit ={
    if (! txtGameChat.getText.isEmpty) {
      Main.clientRef ! SendMessage(Main.mainController.getUserName, txtGameChat.getText)
    }
    txtGameChat.text = ""

  }

  def createChatBubbleClientAtGame(name:String,msg:String): Unit ={
    //add new labels to flow panel
    val borderHBox = new HBox(){
      padding = Insets(5, 10, 5, 10)
      margin =  Insets(5, 10, 5, 10)

    }
    borderHBox.maxWidth = 250

    val chatText = new Text(s"${name}: ${msg}")

    borderHBox.getChildren.add(chatText)
    chatText.wrappingWidthProperty.set(250)
    borderHBox.getStyleClass.add("chat-text-game")
    vBoxGameChat.getChildren.add(borderHBox)
    //set to bottom
    scrollPaneGameChat.vvalueProperty.bind(vBoxGameChat.heightProperty)
  }

  def generateGamePlayerList(userFromList:Iterable[User],readyUserList:Iterable[User]){
    hBoxPlayers.getChildren().clear()
    for(user<-userFromList) {        //add new labels to flow panel
      val borderVBox = new VBox() {
        padding = Insets(10, 10, 10, 10)
        margin = Insets(10, 10, 10, 10)
      }
      borderVBox.maxWidth = 200
      borderVBox.maxHeight = 150

      var chatText = new Text("")

      if (user == userFromList.head) {
        role = "Painter"
      } else {
        role = "Player"
      }
      chatText = new Text(s"${user.name} - ${role}")
      borderVBox.getStyleClass.add("player-list-box")

      borderVBox.getChildren.add(chatText)
      chatText.wrappingWidthProperty.set(200)
      hBoxPlayers.getChildren.add(borderVBox)
    }//end for 2nd
  }


}
