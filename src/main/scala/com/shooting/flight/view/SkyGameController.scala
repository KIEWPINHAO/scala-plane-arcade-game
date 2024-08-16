package com.shooting.flight.view

import com.shooting.flight.MainApp.{playerName, showGameHall}
import com.shooting.flight.model.LeaderboardEntry
import com.shooting.flight.{MainApp, PlaneProperty}
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import javafx.scene.{input => jfxsi}
import javafx.event.EventHandler
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.scene.Scene
import javafx.{scene => jfxs}
import scalafx.application.Platform
import scalafx.scene.control.{Alert, Label}
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.AnchorPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

import java.time.LocalDateTime
import scala.util.Random


@sfxml
class SkyGameController(val plane: ImageView, val gamePane: AnchorPane, val heart1: ImageView, val heart2: ImageView, val heart3: ImageView, val scoring: Label,
                        val pauseLabel : Label, val pauseLabel2: Label){

  //all variables listed here
  var planeSpeed = 0
  var weaponGap = 0
  var multipleWeapon = true

  private var playerInsertName: String = _
  private var isPaused = false
  private var score: Int = 0
  private var lives = 3 // Initial number of lives
  private var invincible = false // Flag for invincibility
  private var gameLoop: Option[AnimationTimer] = None
  private var bullets = List[Rectangle]()
  private var missiles = List[(ImageView, Double)]()

  private val missileImage = new Image("/images/missile.gif") // Replace with your image path
  private val explosionImage = new Image("/images/explode.gif")

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
    updatePlaneImage()
    playerInsertName = MainApp.playerName
  }

  def startGameLoop(): Unit = {
    val timer = AnimationTimer(_ => {
      updateBullets()
      spawnMissiles()
      updateMissiles()
      checkCollisions()
      checkPlaneCollisions()
    })

    gameLoop = Some(timer)
    timer.start()
  }

  def stopGameLoop(): Unit = {
    gameLoop.foreach(_.stop())
    gameLoop = None
  }

  private def updatePlaneImage(): Unit = {
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

      case jfxsi.KeyCode.P =>
        if (isPaused) {
          resumeGame()
        } else {
          pauseGame()
        }

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
    if (Random.nextDouble() < 0.04) { // Adjust spawn rate as needed

      val missile = new ImageView(missileImage) {
        fitWidth = 50
        fitHeight = 100
        layoutX = Random.nextDouble() * (gamePane.width.value - fitWidth.value)
        layoutY = -fitHeight.value
        rotate = 180 // Rotate by a random angle up to 180 degrees
      }

      // Assign a random speed to the missiles
      val speed = 2 + Random.nextDouble() * 4 // Speed between 2 and 5

      // Add the missiles to the game
      missiles = (missile, speed) :: missiles
      gamePane.children.add(missile)
    }
  }

  def updateMissiles(): Unit = {
    // Remove missile that have fallen off the screen
    val missilesToRemove = missiles.filter { case (missile, _) => missile.layoutY.value > gamePane.height.value }
    missilesToRemove.foreach { case (missile, _) =>
      gamePane.children.remove(missile)
    }
    // Filter out the removed missiles
    missiles = missiles.filterNot(missile => missilesToRemove.contains(missile))

    // Update positions of remaining missiles
    missiles.foreach { case (missile, speed) =>
      missile.layoutY = missile.layoutY.value + speed
    }
  }

  def checkCollisions(): Unit = {
    // Iterate over bullets and missile to check for collisions
    bullets.foreach { bullet =>
      missiles.foreach { case (missile, _) =>
        if (isColliding(bullet, missile)) {
          gamePane.children.remove(bullet)
          bullets = bullets.filterNot(_ == bullet)

          // Replace the missile with the explosion image
          val explosion = new ImageView(explosionImage) {
            fitWidth = 90
            fitHeight = 90
            layoutX = missile.layoutX.value - 20
            layoutY = missile.layoutY.value
          }
          gamePane.children.add(explosion)
          gamePane.children.remove(missile)
          missiles = missiles.filterNot { case (a, _) => a == missile }

          // Remove the explosion image after a short delay
          val pauseTransition = new PauseTransition(Duration(500)) { // Explosion visible for 0.5 seconds
            onFinished = _ => gamePane.children.remove(explosion)
          }
          pauseTransition.play()
          score += 75
          refreshScoreLabel()
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

  def refreshScoreLabel(): Unit = {
    scoring.text = s"Score: $score"
  }

  def updateHeartVisibility(): Unit = {
    heart1.visible = lives >= 1
    heart2.visible = lives >= 2
    heart3.visible = lives >= 3
  }

  def checkPlaneCollisions(): Unit = {
    missiles.foreach { case (missile, _) =>
      if (!invincible && isColliding(plane, missile)) {
        gamePane.children.remove(missile)
        missiles = missiles.filterNot { case (a, _) => a == missile }
        lives -= 1
        updateHeartVisibility()
        if (lives <= 0) {
          stopGameLoop() // Stop the game loop
          val entry = LeaderboardEntry(1,playerInsertName, score, LocalDateTime.now())
          LeaderboardEntry.save(entry)
          Platform.runLater{
            MainApp.showEndGame(score)
          }

        } else {
          activateInvincibility()
        }
      }
    }
  }

  def isColliding(plane: ImageView, missile: ImageView): Boolean = {
    val planeBounds = plane.boundsInParent.value
    val missileBounds = missile.boundsInParent.value
    planeBounds.intersects(missileBounds)
  }


  def activateInvincibility(): Unit = {
    if (!invincible) { // Ensure invincibility is not already active
      invincible = true
      val outlineEffect = new DropShadow {
        color = Color.WHITE
        radius = 20
        spread = 0.7
      }
      plane.effect = outlineEffect
      val pauseTransition = new PauseTransition(Duration(2500)) { // 3 seconds invincibility
        onFinished = _ => {
          invincible = false
          plane.effect = null
        }
      }
      pauseTransition.play()
    }
  }

  def pauseGame(): Unit = {
    isPaused = true
    pauseLabel.visible = true // Show the pause label
    pauseLabel2.visible = true
    stopGameLoop() // Stop the game loop
  }

  def resumeGame(): Unit = {
    isPaused = false
    pauseLabel.visible = false // Show the pause label
    pauseLabel2.visible = false
    startGameLoop() // Resume the game loop
  }

}