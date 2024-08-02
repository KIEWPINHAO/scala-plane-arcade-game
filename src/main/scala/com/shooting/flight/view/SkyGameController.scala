package com.shooting.flight.view



import com.shooting.flight.PlaneProperty
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import javafx.scene.{input => jfxsi}
import javafx.event.EventHandler
import scalafx.animation.AnimationTimer
import scalafx.scene.layout.AnchorPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle


@sfxml
class SkyGameController(val plane: ImageView, val gamePane: AnchorPane){

  private var bullets = List[Rectangle]()
  var planeSpeed = 0
  var weaponGap = 0
  val RedPlane = new Image("/images/redShip.png")
  plane.image = RedPlane
  private var gameLoop: Option[AnimationTimer] = None

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
    startGameLoop()
    updateSpaceshipImage()
  }

  private def updateSpaceshipImage(): Unit = {
    PlaneProperty.updateSelectedPlane()
    PlaneProperty.getSelectedPlane match {
      case Some(planeProperty) =>
        plane.image = planeProperty.image
        planeSpeed = planeProperty.speed
        weaponGap = planeProperty.weaponGap
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
        val newX = plane.layoutX.value - planeSpeed
        if (newX >= 0) { // Check if the new position is within the left boundary
          plane.layoutX = newX
        }

      case jfxsi.KeyCode.RIGHT =>
        val newX = plane.layoutX.value + planeSpeed
        if (newX <= (gamePane.width.value - plane.fitWidth())) { // Check if the new position is within the right boundary
          plane.layoutX = newX
        }

      case jfxsi.KeyCode.SPACE =>
        shoot()

      case _ =>
    }
  }

  def handleKeyRelease(event: jfxsi.KeyEvent): Unit = {
    // Optionally handle key releases if needed
  }

  def shoot(): Unit = {
    // Create the first bullet
    val bullet1 = new Rectangle {
      width = 5
      height = 10
      fill = Color.Red
      layoutX = plane.layoutX.value + plane.fitWidth() / 2 - 2.5 - weaponGap
      layoutY = plane.layoutY.value - 10
    }

    // Create the second bullet, slightly offset from the first
    val bullet2 = new Rectangle {
      width = 5
      height = 10
      fill = Color.Red
      layoutX = plane.layoutX.value + plane.fitWidth() / 2 - 2.5 + weaponGap // Adjust offset as needed
      layoutY = plane.layoutY.value - 10
    }

    // Add both bullets to the game pane and the list of bullets
    bullets = bullet1 :: bullet2 :: bullets
    gamePane.children.addAll(bullet1,bullet2)
  }

  def startGameLoop(): Unit = {
    val timer = AnimationTimer(_ => {
      updateBullets()
    })

    gameLoop = Some(timer)
    timer.start()
  }

  def stopGameLoop(): Unit = {
    gameLoop.foreach(_.stop())
    gameLoop = None
  }

  def updateBullets(): Unit = {
    val bulletsToRemove = bullets.filter(_.layoutY.value < 0)
    bulletsToRemove.foreach { bullet =>
      gamePane.children.remove(bullet)
      bullets = bullets.filterNot(_ == bullet)
    }
    bullets.foreach { bullet =>
      bullet.layoutY = bullet.layoutY.value - 5
    }
  }


}