name := "testKafka"

version := "0.2"

scalaVersion := "2.12.9"

lazy val testkafka = project
  .in(file("."))
  .aggregate((
    producer
    ))


lazy val producer = (project in file("producer"))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    version := "0.3",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.typesafe" % "config" % "1.3.1",
      "org.apache.kafka" % "kafka-clients" % "2.3.0"
    )
  )