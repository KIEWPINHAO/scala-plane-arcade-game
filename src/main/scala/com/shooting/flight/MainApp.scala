package com.shooting.flight

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.scene.input.KeyEvent



object MainApp extends JFXApp {

  // Load the GameLobby.fxml directly
  val lobbyResource = getClass.getResource("view/GameHall.fxml")
  val loader = new FXMLLoader(lobbyResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.AnchorPane]

  // Set up the primary stage with the game lobby
  stage = new PrimaryStage {
    title = "Ace of the Sky"
    scene = new Scene {
      root = roots
    }
  }

  def showGame(): Unit = {
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
}