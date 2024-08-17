package com.shooting.flight

import com.shooting.flight.model.LeaderboardEntry
import com.shooting.flight.util.Database
import com.shooting.flight.view.EndGameController
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.scene.input.KeyEvent
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.stage.{Modality, Stage}



object MainApp extends JFXApp {
  Database.setupDB()

  val leaderBoard = new ObservableBuffer[LeaderboardEntry]()

  var playerName : String = "x"

  val backgroundMusic = new Media(getClass.getResource("/musics/bgMusic_apex.mp3").toString)
  val player = new MediaPlayer(backgroundMusic)
  player.setCycleCount(MediaPlayer.Indefinite)
  player.play()

  val lobbyResource = getClass.getResource("view/GameHall.fxml")
  val loader = new FXMLLoader(lobbyResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.AnchorPane]

  // Set up the primary stage with the game lobby
  stage = new PrimaryStage {
    title = "Ace of the Sky"
    icons += new Image(getClass.getResourceAsStream("/images/redPlane.png"))
    scene = new Scene {
      root = roots
    }
  }

  def showGame(): Unit = {
    player.stop()
    val gameResource = getClass.getResource("view/SkyGame.fxml")
    val gameLoader = new FXMLLoader(gameResource, NoDependencyResolver)
    gameLoader.load()
    val gameRoots = gameLoader.getRoot[jfxs.layout.AnchorPane]
    stage.scene = new Scene {
      root = gameRoots
      onKeyPressed = (event: KeyEvent) => {
        gameRoots.delegate.lookup("#plane").requestFocus()
      }
    }
  }

  def showChoose(): Unit = {
    val choose = getClass.getResource("view/ChoosePlane.fxml")
    val loader = new FXMLLoader(choose, NoDependencyResolver)
    loader.load()
    val chooseRoot = loader.getRoot[jfxs.layout.AnchorPane]
    stage.scene = new Scene {
      root = chooseRoot
    }
  }

  def showGameHall(): Unit = {
    val hall = getClass.getResource("view/GameHall.fxml")
    val loader = new FXMLLoader(hall, NoDependencyResolver)
    loader.load()
    val hallRoot = loader.getRoot[jfxs.layout.AnchorPane]
    stage.scene = new Scene {
      root = hallRoot
    }
  }

  def showLeaderboards(): Unit = {
    leaderBoard.clear()
    leaderBoard ++= LeaderboardEntry.getAllEntries
    val leader = getClass.getResource("view/Leaderboards.fxml")
    val loader = new FXMLLoader(leader, NoDependencyResolver)
    loader.load()
    val leaderRoot = loader.getRoot[jfxs.layout.AnchorPane]
    stage.scene = new Scene {
      root = leaderRoot
    }
  }

  def showHelp(): Unit = {
    val help = getClass.getResource("view/Help.fxml")
    val loader = new FXMLLoader(help, NoDependencyResolver)
    loader.load()
    val helpRoot = loader.getRoot[jfxs.layout.AnchorPane]
    stage.scene = new Scene {
      root = helpRoot
    }
  }

  def showEndGame(score: Int): Unit = {
    val resource = getClass.getResourceAsStream("view/EndGame.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[EndGameController#Controller]

    control.setScore(score)

    val dialog = new Stage() {
      title = "Game Over"
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        stylesheets += getClass.getResource("view/BrownPixelTheme.css").toString
        root = roots2
      }
    }
    control.dialogStage = dialog
    dialog.showAndWait()
    control.okClicked
  }

  val buttonSound1 = new Media(getClass.getResource("/sounds/buttonSound1.mp3").toString)
  val buttonSoundEffect1 = new MediaPlayer(buttonSound1)

  def playButtonSound1(): Unit = {
    buttonSoundEffect1.stop()
    buttonSoundEffect1.play()
  }


}