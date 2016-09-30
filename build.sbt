organization := "com.dragisak"

name := "modeling-with-free"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  Resolver.bintrayRepo("projectseptemberinc", "maven"),
  Resolver.sonatypeRepo("releases"),
  Resolver.jcenterRepo
)

libraryDependencies ++= Seq(
  "com.thangiee" %% "freasy-monad" % "0.4.0",
  "com.projectseptember" %% "freek" % "0.6.1",
  "org.typelevel" %% "cats" % "0.7.2",
  "org.scalacheck" %% "scalacheck" % "1.13.2" % Test,
  "org.scalatest" %% "scalatest" % "3.0.0" % Test,
  "com.ironcorelabs" %% "cats-scalatest" % "1.4.0" % Test
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

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.0")

addCompilerPlugin("com.milessabin" % "si2712fix-plugin" % "1.2.0" cross CrossVersion.full)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

scalastyleFailOnError := true
