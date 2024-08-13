package com.shooting.flight.model

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import com.shooting.flight.util.Database
import scalikejdbc._
import java.time.LocalDateTime

import scala.util.{Failure, Success, Try}

class LeaderboardEntry(val id: Int, val playerName: String, val score: Int, val date: LocalDateTime = LocalDateTime.now()) extends  Database {

  var idProperty = new StringProperty(id.toString)
  var nameProperty = new StringProperty(playerName)
  var scoreProperty = new StringProperty(score.toString)
  var dateProperty = new StringProperty(date.toString)

  def update(): Try[Int] = {
    Try {
      DB autoCommit { implicit session =>
        sql"""
          UPDATE Leaderboard SET
            playerName = ${nameProperty.value},
            score = ${scoreProperty.value.toInt},
            date = ${LocalDateTime.parse(dateProperty.value)}
          WHERE id = ${id}
        """.update.apply()
      }
    }
  }

  def delete(): Try[Int] = {
    Try {
      DB autoCommit { implicit session =>
        sql"""
          DELETE FROM Leaderboard WHERE id = ${id}
        """.update.apply()
      }
    }
  }
}

object LeaderboardEntry extends Database {

  def apply(id: Int, playerName: String, score: Int, date: LocalDateTime): LeaderboardEntry = {
    new LeaderboardEntry(id, playerName, score, date)
  }

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        CREATE TABLE Leaderboard (
          id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
          playerName VARCHAR(64) NOT NULL,
          score INT NOT NULL,
          date TIMESTAMP NOT NULL
        )
      """.execute.apply()
    }
  }

  def save(entry: LeaderboardEntry): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        INSERT INTO Leaderboard (playerName, score, date) VALUES
        (${entry.playerName}, ${entry.score}, ${entry.date})
      """.update.apply()
    }

  }


  def getAllEntries: List[LeaderboardEntry] = {
    DB readOnly { implicit session =>
      sql"SELECT id, playerName, score, date FROM Leaderboard ORDER BY score DESC"
        .map { rs =>
          new LeaderboardEntry(
            rs.int("id"),
            rs.string("playerName"),
            rs.int("score"),
            rs.localDateTime("date")
          )
        }.list.apply()
    }
  }
}
