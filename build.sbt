organization := "com.dragisak"

name := "modeling-with-free"

version := "1.0"

scalaVersion := "2.12.8"

resolvers ++= Seq(
  Resolver.bintrayRepo("projectseptemberinc", "maven"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.jcenterRepo
)

libraryDependencies ++= Seq(
  "com.github.thangiee" %% "freasy-monad" % "0.6.0",
  "com.projectseptember" %% "freek" % "0.6.7",
  "org.typelevel" %% "cats" % "0.9.0",
  "org.scalacheck" %% "scalacheck" % "1.13.5" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "com.ironcorelabs" %% "cats-scalatest" % "2.2.0" % Test
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-Xfatal-warnings",
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

addCompilerPlugin(
  "org.scalameta" % "paradise" % "3.0.0-M11" cross CrossVersion.full
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

coverageHighlighting := false

scalastyleFailOnError := true

logBuffered in Test := false
