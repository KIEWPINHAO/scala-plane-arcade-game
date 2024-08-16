package com.shooting.flight

import scalafx.scene.image.Image

case class Plane(image: Image, speed: Int, twoWeapon: Boolean ,weaponGap: Int, speedRating: Int, weaponRating: Int)

object PlaneProperty {
  var currentIndex: Int = 0

  val redPlane = Plane(new Image("/images/redPlane.png"), 15, twoWeapon = true ,10, 2, 2)
  val bluePlane = Plane(new Image("/images/bluePlane.png"), 20, twoWeapon = false ,0, 3, 1)
  val blackPlane = Plane(new Image("/images/blackPlane.png"), 10, twoWeapon = true,20, 1, 3)

  val planes = List(redPlane, bluePlane, blackPlane)


  private var selectedPlane: Option[Plane] = None

  // Method to get a plane by index
  def getPlaneByIndex(index: Int): Option[Plane] = {
    if (index >= 0 && index < planes.length) {
      Some(planes(index))
    } else {
      None

    }
  }

  // Method to update the selected plane based on current index
  def updateSelectedPlane(): Unit = {
    selectedPlane = getPlaneByIndex(currentIndex)
  }

  // Method to get the selected plane
  def getSelectedPlane: Option[Plane] = selectedPlane


}