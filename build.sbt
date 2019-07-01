organization := "com.dragisak"

name := "modeling-with-free"

version := "1.0"

scalaVersion := "2.12.8"

val catsVersion = "2.0.0-M4"

libraryDependencies ++= Seq(
  "org.typelevel"    %% "cats-free"      % catsVersion,
  "org.scalacheck"   %% "scalacheck"     % "1.14.0" % Test,
  "org.scalatest"    %% "scalatest"      % "3.0.8" % Test,
  "com.ironcorelabs" %% "cats-scalatest" % "2.4.1" % Test
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-unchecked",
  // "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Ywarn-unused-import",
  "-Xfuture",
  "-language:existentials",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-Ypartial-unification"
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
