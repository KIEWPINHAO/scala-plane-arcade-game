package com.shooting.flight

import scalafx.scene.image.Image

case class Plane(image: Image, speed: Int, weaponGap: Int)

object PlaneProperty {
  var currentIndex: Int = 0

  val redShip = Plane(new Image("/images/redShip.png"), 18, 10)
  val blackShip = Plane(new Image("/images/blackShip.png"), 13, 20)

  val ships = List(redShip, blackShip)

  private var selectedShip: Option[Plane] = None

  // Method to get a ship by index
  def getPlaneByIndex(index: Int): Option[Plane] = {
    if (index >= 0 && index < ships.length) {
      Some(ships(index))
    } else {
      None
    }
  }

  // Method to update the selected ship based on current index
  def updateSelectedPlane(): Unit = {
    selectedShip = getPlaneByIndex(currentIndex)
  }

  // Method to get the selected ship
  def getSelectedPlane: Option[Plane] = selectedShip


}