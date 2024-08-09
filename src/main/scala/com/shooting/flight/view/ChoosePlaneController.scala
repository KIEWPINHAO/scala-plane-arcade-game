package com.shooting.flight.view

import com.shooting.flight.MainApp
import com.shooting.flight.PlaneProperty
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.image.ImageView
import scalafx.scene.control.{Alert, Button, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class ChoosePlaneController(val redShip: ImageView, val blueShip: ImageView,val blackShip: ImageView,
                         val previous: Button, val next: Button, val weaponStar1: ImageView, val weaponStar2: ImageView, val weaponStar3: ImageView,
                            val speedStar1: ImageView, val speedStar2: ImageView, val speedStar3: ImageView, val name: TextField) {

  private val ships = List(redShip, blueShip, blackShip)
  PlaneProperty.currentIndex = 0
  PlaneProperty.getSelectedPlane

  // Initialize the controller
  initializeShip()

  def initializeShip(): Unit = {
    updateShips()
    updateButtons()
  }

  def updateShips(): Unit = {
    ships.zipWithIndex.foreach { case (ship, index) =>
      ship.visible = index == PlaneProperty.currentIndex
    }
    println(PlaneProperty.currentIndex)
  }

  def updateButtons(): Unit = {
    previous.disable = PlaneProperty.currentIndex == 0
    next.disable = PlaneProperty.currentIndex == ships.length - 1
  }

  def goPre(): Unit = {
    if (PlaneProperty.currentIndex > 0) {
      PlaneProperty.currentIndex -= 1
      updateShips()
      updateButtons()
    }
  }

  def goNext(): Unit = {
    if (PlaneProperty.currentIndex < ships.length - 1) {
      PlaneProperty.currentIndex += 1
      updateShips()
      updateButtons()
    }
  }

  def getCurrentIndex: Int = {
    PlaneProperty.currentIndex
  }

  def goHome(): Unit = {
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
      MainApp.showGame()
    }
  }
}