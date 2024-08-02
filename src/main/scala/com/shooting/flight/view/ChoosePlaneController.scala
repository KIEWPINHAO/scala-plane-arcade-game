package com.shooting.flight.view

import com.shooting.flight.MainApp
import com.shooting.flight.PlaneProperty
import scalafx.scene.image.ImageView
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml

@sfxml
class ChoosePlaneController(val redShip: ImageView, val blackShip: ImageView,
                         val previous: Button, val next: Button) {

  private val ships = List(redShip, blackShip)
  PlaneProperty.currentIndex = 0

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
    MainApp.showGame()
  }
}