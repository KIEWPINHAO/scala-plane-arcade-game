package com.shooting.flight

import scalafx.scene.image.Image

case class Plane(image: Image, speed: Int, twoWeapon: Boolean ,weaponGap: Int, speedRating: Int, weaponRating: Int)

object PlaneProperty {
  var currentIndex: Int = 0

  val redShip = Plane(new Image("/images/redShip.png"), 15, twoWeapon = true ,10, 2, 2)
  val blueShip = Plane(new Image("/images/blueShip.png"), 20, twoWeapon = false ,0, 3, 1)
  val blackShip = Plane(new Image("/images/blackShip.png"), 10, twoWeapon = true,20, 1, 3)

  val ships = List(redShip, blueShip, blackShip)

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