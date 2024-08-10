package com.shooting.flight.view

import com.shooting.flight.MainApp
import scalafx.event.ActionEvent
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.scene.control.Button
import scalafx.scene.control.Label

@sfxml
class EndGameController(val closeButton: Button, val endScore: Label) {

  // Reference to the stage of the pop-up window
  private var popupStage: Stage = _
  var         dialogStage : Stage  = null
  var         okClicked            = false

  def setScore(score: Int): Unit = {
    endScore.text = s"Score: $score"
  }

  // Method to set the stage (to be called from the main controller)
  def setStage(stage: Stage): Unit = {
    this.popupStage = stage
  }

  def handleOk(action :ActionEvent){
    okClicked = true;
    dialogStage.close()
    MainApp.showGameHall()
  }
}