package com.shooting.flight

import scalafx.scene.image.Image
import scalafx.scene.paint.Color

// Base class for Plane with a type parameter for Bullet Color
abstract class Plane[B](val image: Image, val speed: Int, val weaponGap: Int, val speedRating: Int, val weaponRating: Int) {
  def twoWeapon: Boolean
  def bulletColor: B
}

// Subclasses for different planes with specific bullet colors
class RedPlane extends Plane[Color](
  new Image("/images/redPlane.png"),
  speed = 15,
  weaponGap = 10,
  speedRating = 2,
  weaponRating = 2
) {
  override def twoWeapon: Boolean = true
  override def bulletColor: Color = Color.Red
}

class BluePlane extends Plane[Color](
  new Image("/images/bluePlane.png"),
  speed = 20,
  weaponGap = 0,
  speedRating = 3,
  weaponRating = 1
) {
  override def twoWeapon: Boolean = false
  override def bulletColor: Color = Color.Blue
}

class BlackPlane extends Plane[Color](
  new Image("/images/blackPlane.png"),
  speed = 10,
  weaponGap = 20,
  speedRating = 1,
  weaponRating = 3
) {
  override def twoWeapon: Boolean = true
  override def bulletColor: Color = Color.White
}

object PlaneProperty {
  var currentIndex: Int = 0

  val redPlane = new RedPlane
  val bluePlane = new BluePlane
  val blackPlane = new BlackPlane

  val planes = List(redPlane, bluePlane, blackPlane)

  private var selectedPlane: Option[Plane[Color]] = None

  // Method to get a plane by index
  def getPlaneByIndex(index: Int): Option[Plane[Color]] = {
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
  def getSelectedPlane: Option[Plane[Color]] = selectedPlane
}
