package com.shooting.flight.util


import com.shooting.flight.model.LeaderboardEntry
import scalikejdbc._

trait Database {

  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  // Set up the database connection details
  val dbURL = "jdbc:derby:leaderboardDB;create=true;" // Using Apache Derby for an embedded database

  // Initialize the JDBC driver and connection pool
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")

  implicit val session: AutoSession.type = AutoSession

  // This function will set up the database table if it doesn't exist
}

object Database extends Database{
  def setupDB() = {
    if (!hasDBInitialize)
      LeaderboardEntry.initializeTable()
  }
  def hasDBInitialize : Boolean = {

    DB getTable "LEADERBOARD" match {
      case Some(x) => true
      case None => false
    }

  }
}