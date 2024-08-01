package com.shooting.flight.view


import com.shooting.flight.PlaneProperty.index
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import javafx.scene.{input => jfxsi}
import javafx.event.EventHandler
import scalafx.scene.layout.AnchorPane


@sfxml
class SkyGameController(val plane: ImageView, val gamePane: AnchorPane){

  val RedPlane = new Image("/images/redShip.png")
  plane.image = RedPlane
  println(index)

  initialize()

  def initialize(): Unit = {
    // Set up key event handling
    if (plane.scene() != null) {
      addEventHandlers()
    } else {
      // Delay adding event filters until the scene is set
      plane.sceneProperty().addListener((_, _, newScene) => {
        if (newScene != null) {
          addEventHandlers()
        }
      })
    }


  }

  private def addEventHandlers(): Unit = {
    plane.scene().addEventFilter(jfxsi.KeyEvent.KEY_PRESSED, new EventHandler[jfxsi.KeyEvent] {
      override def handle(event: jfxsi.KeyEvent): Unit = handleKeyPress(event)
    })
    plane.scene().addEventFilter(jfxsi.KeyEvent.KEY_RELEASED, new EventHandler[jfxsi.KeyEvent] {
      override def handle(event: jfxsi.KeyEvent): Unit = handleKeyRelease(event)
    })
  }

  def handleKeyPress(event: jfxsi.KeyEvent): Unit = {
    event.getCode match {
      case jfxsi.KeyCode.LEFT =>
        val newX = plane.layoutX.value - 10
        if (newX >= 0) { // Check if the new position is within the left boundary
          plane.layoutX = newX
        }

      case jfxsi.KeyCode.RIGHT =>
        val newX = plane.layoutX.value + 10
        if (newX <= (gamePane.width.value - plane.fitWidth())) { // Check if the new position is within the right boundary
          plane.layoutX = newX
        }

      case _ =>
    }
  }

  def handleKeyRelease(event: jfxsi.KeyEvent): Unit = {
    // Optionally handle key releases if needed
  }

}