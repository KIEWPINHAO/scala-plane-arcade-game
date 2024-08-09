package com.shooting.flight.view


import com.shooting.flight.MainApp
import scalafx.application.Platform
import scalafxml.core.macros.sfxml


@sfxml
class GameHallController(){

  def getStart(): Unit = {
    MainApp.showChoose()
  }

  def goHome(): Unit = {
    MainApp.showGameHall()
  }

  def goLeader(): Unit = {
    MainApp.showLeaderboards()
  }

  def goHelp(): Unit = {
    MainApp.showHelp()
  }

  def goExit(): Unit = {
    Platform.exit()
  }

}