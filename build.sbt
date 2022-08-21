lazy val root = (project in file("."))
  .settings(
    name := "subcontacts"
  )

version := "0.1"

scalaVersion := "2.13.2"

val akkaVersion       = "2.5.25"
val akkaHttpVersion   = "10.1.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "io.spray"          %% "spray-json"           % "1.3.5",
  "com.typesafe.akka" %% "akka-slf4j"           % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
  "ch.qos.logback"    %  "logback-classic"      % "1.1.2",

  "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
  "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
  "org.scalatest"     %% "scalatest"            % "3.1.4"         % Test

)
