package com.shooting.flight.view

import com.shooting.flight.PlaneProperty
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import javafx.scene.{input => jfxsi}
import javafx.event.EventHandler
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

import scala.util.Random


@sfxml
class SkyGameController(val plane: ImageView, val gamePane: AnchorPane){

  //all variables listed here
  private var bullets = List[Rectangle]()
  var planeSpeed = 0
  var weaponGap = 0
  var multipleWeapon = true
  private val missileImage = new Image("/images/missile.gif") // Replace with your image path
  private var missiles = List[(ImageView, Double)]()
  private val explosionImage = new Image("/images/explode.gif")
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

  def startGameLoop(): Unit = {
    val timer = AnimationTimer(_ => {
      updateBullets()
      spawnMissiles()
      updateMissiles()
      checkCollisions()
    })

    gameLoop = Some(timer)
    timer.start()
  }

  def stopGameLoop(): Unit = {
    gameLoop.foreach(_.stop())
    gameLoop = None
  }

  private def updateSpaceshipImage(): Unit = {
    PlaneProperty.updateSelectedPlane()
    PlaneProperty.getSelectedPlane match {
      case Some(planeProperty) =>
        plane.image = planeProperty.image
        planeSpeed = planeProperty.speed
        weaponGap = planeProperty.weaponGap
        multipleWeapon = planeProperty.twoWeapon
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

    bullets = bullet1 :: bullets
    gamePane.children.add(bullet1)

    if (multipleWeapon) {
      // Create the second bullet, slightly offset from the first
      val bullet2 = new Rectangle {
        width = 5
        height = 10
        fill = Color.Red
        layoutX = plane.layoutX.value + plane.fitWidth() / 2 - 2.5 + weaponGap // Adjust offset as needed
        layoutY = plane.layoutY.value - 10
      }

      // Add the second bullet to the game pane and the list of bullets
      bullets = bullet2 :: bullets
      gamePane.children.add(bullet2)
    }
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

  def spawnMissiles(): Unit = {
    if (Random.nextDouble() < 0.02) { // Adjust spawn rate as needed

      val missile = new ImageView(missileImage) {
        fitWidth = 50
        fitHeight = 100
        layoutX = Random.nextDouble() * (gamePane.width.value - fitWidth.value)
        layoutY = -fitHeight.value
        rotate = 180 // Rotate by a random angle up to 180 degrees
      }

      // Assign a random speed to the asteroid
      val speed = 2 + Random.nextDouble() * 4 // Speed between 2 and 5

      // Add the asteroid to the game
      missiles = (missile, speed) :: missiles
      gamePane.children.add(missile)
    }
  }

  def updateMissiles(): Unit = {
    // Remove asteroids that have fallen off the screen
    val asteroidsToRemove = missiles.filter { case (asteroid, _) => asteroid.layoutY.value > gamePane.height.value }
    asteroidsToRemove.foreach { case (asteroid, _) =>
      gamePane.children.remove(asteroid)
    }
    // Filter out the removed asteroids
    missiles = missiles.filterNot(asteroid => asteroidsToRemove.contains(asteroid))

    // Update positions of remaining asteroids
    missiles.foreach { case (asteroid, speed) =>
      asteroid.layoutY = asteroid.layoutY.value + speed
    }
  }

  def checkCollisions(): Unit = {
    // Iterate over bullets and asteroids to check for collisions
    bullets.foreach { bullet =>
      missiles.foreach { case (asteroid, _) =>
        if (isColliding(bullet, asteroid)) {
          gamePane.children.remove(bullet)
          bullets = bullets.filterNot(_ == bullet)

          // Replace the asteroid with the explosion image
          val explosion = new ImageView(explosionImage) {
            fitWidth = 90
            fitHeight = 90
            layoutX = asteroid.layoutX.value - 20
            layoutY = asteroid.layoutY.value
          }
          gamePane.children.add(explosion)
          gamePane.children.remove(asteroid)
          missiles = missiles.filterNot { case (a, _) => a == asteroid }

          // Remove the explosion image after a short delay
          val pauseTransition = new PauseTransition(Duration(500)) { // Explosion visible for 0.5 seconds
            onFinished = _ => gamePane.children.remove(explosion)
          }
          pauseTransition.play()
        }
      }
    }
  }

  // Overloaded methods for collision detection
  def isColliding(bullet: Rectangle, missile: ImageView): Boolean = {
    val bulletBounds = bullet.boundsInParent.value
    val missileBounds = missile.boundsInParent.value
    bulletBounds.intersects(missileBounds)
  }


}