package com.shooting.flight.view


import com.shooting.flight.MainApp
import com.shooting.flight.PlaneProperty
import scalafxml.core.macros.sfxml


@sfxml
class GameHallController(){

  def getStart(): Unit = {
    MainApp.showChoose()
  }

}