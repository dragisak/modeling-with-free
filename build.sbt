organization := "com.dragisak"

name := "modeling-with-free"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.7.2",
  "org.scalacheck" %% "scalacheck" % "1.13.2" % Test,
  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)


scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-unchecked",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code", // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused-import",
  "-Xfuture",
  "-language:existentials",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions"
)


testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

scalastyleFailOnError := true
