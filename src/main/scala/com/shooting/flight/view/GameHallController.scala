package com.shooting.flight.view


import com.shooting.flight.MainApp
import scalafx.application.Platform
import scalafxml.core.macros.sfxml


@sfxml
class GameHallController(){

  def getStart(): Unit = {
    MainApp.showChoose()
    MainApp.playButtonSound1()
  }

  def goHome(): Unit = {
    MainApp.showGameHall()
    MainApp.playButtonSound1()
  }

  def goLeader(): Unit = {
    MainApp.showLeaderboards()
    MainApp.playButtonSound1()
  }

  def goHelp(): Unit = {
    MainApp.showHelp()
    MainApp.playButtonSound1()
  }

  def goExit(): Unit = {
    Platform.exit()
  }

}