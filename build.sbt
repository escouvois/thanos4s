import Libs._

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / scalacOptions ++= Seq(
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps"
)

val libs = Seq(
  betterFilesLib,
  catsLib,
  catsEffectLib,
  declineLib,
  pprintLib,
  pureConfigLib
)

lazy val root = (project in file("."))
  .settings(name := "thanos")
  .settings(libs: _*)
  .enablePlugins(PackPlugin)
