package com.shooting.flight.view


import com.shooting.flight.MainApp
import com.shooting.flight.PlaneProperty
import scalafxml.core.macros.sfxml


@sfxml
class GameHallController(){
  PlaneProperty.index = 1

  def getStart(): Unit = {
    MainApp.showGame()
  }

}