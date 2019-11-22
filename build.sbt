name := "DS_DrawSomething"

version := "0.1"

scalaVersion := "2.12.7"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % "2.5.26",
  "com.typesafe.akka" %% "akka-remote" % "2.5.26",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.26",
  "org.scalafx" %% "scalafx" % "12.0.2-R18",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.4")

//fyi, if your java version is after 11, javafx is no longer bundled with it, so need to install separately
// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.18"


// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>
  "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName
)