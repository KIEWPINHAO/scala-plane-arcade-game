package com.shooting.flight.view

import com.shooting.flight.MainApp
import com.shooting.flight.model.LeaderboardEntry
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.beans.property.{IntegerProperty, ReadOnlyIntegerWrapper, StringProperty}
import scalafxml.core.macros.sfxml


@sfxml
class LeaderboardsController(   val leaderboardTable: TableView[LeaderboardEntry],
                                val rankColumn: TableColumn[LeaderboardEntry, String],
                                val nameColumn: TableColumn[LeaderboardEntry, String],
                                val scoreColumn: TableColumn[LeaderboardEntry, String],
                                val dateColumn: TableColumn[LeaderboardEntry, String]) {

  initialize()

  def initialize(): Unit = {
    LeaderboardEntry.getAllEntries

    leaderboardTable.items = MainApp.leaderBoard

    // Set rank column to use the index of the item + 1 as the rank
    rankColumn.cellValueFactory = { cellData =>
      val index = leaderboardTable.getItems.indexOf(cellData.value) + 1
      new StringProperty(index.toString) // Convert index to IntegerProperty
    }

    nameColumn.cellValueFactory = { _.value.nameProperty }
    scoreColumn.cellValueFactory = { _.value.scoreProperty }
    dateColumn.cellValueFactory = { _.value.dateProperty }
  }


  def goHome(): Unit = {
    MainApp.showGameHall()
    MainApp.playButtonSound1()
  }

}
