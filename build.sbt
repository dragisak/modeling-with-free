organization := "com.dragisak"

name := "modeling-with-free"

version := "1.0"

scalaVersion := "2.12.8"

val catsVersion    = "2.0.0-M4"
val zioVersion     = "1.0.0-RC9"
val zioCatsVersion = "1.3.1.0-RC2"

libraryDependencies ++= Seq(
  "org.typelevel"  %% "cats-free"        % catsVersion,
  "org.typelevel"  %% "cats-effect"      % catsVersion,
  "dev.zio"        %% "zio"              % zioVersion,
  "dev.zio"        %% "zio-interop-cats" % zioCatsVersion,
  "org.scalacheck" %% "scalacheck"       % "1.14.0" % Test,
  "org.scalatest"  %% "scalatest"        % "3.0.8" % Test,
  "org.typelevel"  %% "cats-testkit"     % catsVersion % Test
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

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
