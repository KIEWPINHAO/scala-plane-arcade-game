package com.shooting.flight.view


import com.shooting.flight.MainApp
import scalafxml.core.macros.sfxml


@sfxml
class GameHallController(){

  def getStart(): Unit = {
    MainApp.showGame()
  }

}