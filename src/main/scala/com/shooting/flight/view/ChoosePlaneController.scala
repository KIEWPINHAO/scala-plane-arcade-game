package com.shooting.flight.view

import com.shooting.flight.MainApp
import com.shooting.flight.PlaneProperty
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.image.ImageView
import scalafx.scene.control.{Alert, Button, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class ChoosePlaneController(val redPlane: ImageView, val bluePlane: ImageView, val blackPlane: ImageView,
                            val previous: Button, val next: Button, val weaponStar1: ImageView, val weaponStar2: ImageView, val weaponStar3: ImageView,
                            val speedStar1: ImageView, val speedStar2: ImageView, val speedStar3: ImageView, val name: TextField) {

  private var playerName: String = ""
  private val planes = List(redPlane, bluePlane, blackPlane)
  var weaponRating = 0
  var speedRating = 0
  PlaneProperty.currentIndex = 0
  PlaneProperty.getSelectedPlane

  // Initialize the controller
  initializePlane()

  def initializePlane(): Unit =   {
    updatePlane()
    updateButtons()
    updateStars()
  }

  def updatePlane(): Unit = {
    planes.zipWithIndex.foreach { case (plane, index) =>
      plane.visible = index == PlaneProperty.currentIndex
    }
    updatePlaneRating()
//    println(PlaneProperty.currentIndex)
  }

  def updateButtons(): Unit =  {
    previous.disable = PlaneProperty.currentIndex == 0
    next.disable = PlaneProperty.currentIndex == planes.length - 1
  }

  def goPre(): Unit = {
    MainApp.playButtonSound1()
    if (PlaneProperty.currentIndex > 0) {
      PlaneProperty.currentIndex -= 1
      updatePlane()
      updateButtons()
      updateStars()
    }
  }

  def goNext(): Unit = {
    MainApp.playButtonSound1()
    if (PlaneProperty.currentIndex < planes.length - 1) {
      PlaneProperty.currentIndex += 1
      updatePlane()
      updateButtons()
      updateStars()
    }
  }

  private def updatePlaneRating(): Unit = {
    PlaneProperty.updateSelectedPlane()
    PlaneProperty.getSelectedPlane match {
      case Some(planeProperty) =>
        weaponRating = planeProperty.weaponRating
        speedRating = planeProperty.speedRating
        updateStars()
    }
  }

  def updateStars(): Unit = {
    // Update weapon stars based on weaponRating
    weaponStar1.visible = weaponRating >= 1
    weaponStar2.visible = weaponRating >= 2
    weaponStar3.visible = weaponRating >= 3

    // Update speed stars based on speedRating
    speedStar1.visible = speedRating >= 1
    speedStar2.visible = speedRating >= 2
    speedStar3.visible = speedRating >= 3
  }

  def getCurrentIndex: Int = {
    PlaneProperty.currentIndex
  }

  def goHome(): Unit = {
    MainApp.playButtonSound1()
    MainApp.showGameHall()
  }

  def proceed(): Unit = {
    if (name.text.value.trim.isEmpty) {
      // Show an alert or message indicating that the name must be filled
      val alert = new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "Input Required"
        headerText = "Name is required"
        contentText = "Please enter your name before proceeding."
      }
      alert.showAndWait()
    } else {
      // If the text field is not empty, proceed to the next screen
      MainApp.playButtonSound1()
      updatePlayerName()
      MainApp.showGame()
    }
  }

  def updatePlayerName(): Unit = {
    playerName = name.text.value
    MainApp.playerName = playerName
  }


}
