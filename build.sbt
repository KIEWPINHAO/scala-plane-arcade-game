ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.19"

lazy val root = (project in file("."))
  .settings(
    name := "Ace of the Sky",
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafx" % "8.0.192-R14",
      "org.scalafx" %% "scalafxml-core-sfx8" % "0.5",
      "org.apache.derby" % "derby" % "10.12.1.1",
      "org.scalikejdbc" %% "scalikejdbc"       % "4.3.0",
      "com.h2database"  %  "h2"                % "2.2.224",
    )
  )
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)