import Versions._
import sbt.CrossVersion
import sbt.SettingsDefinition
import sbt.addCompilerPlugin
import sbt._
import sbt.Keys.libraryDependencies

object Versions {

  val betterFilesV = "3.9.1"
  val catsV        = "2.5.0"
  val catsEffectV  = "3.0.1"
  val declineV     = "2.0.0"
  val pprintV      = "0.6.4"
  val pureConfigV  = "0.14.1"

  // Plugins.
  val betterMonadicV = "0.3.1"
  val kindProjectorV = "0.11.3"
}

object Libs {

  // BetterFiles (MIT): https://github.com/pathikrit/better-files
  val betterFilesLib: SettingsDefinition =
    libraryDependencies += "com.github.pathikrit" %% "better-files" % betterFilesV

  // Cats (MIT): https://github.com/typelevel/cats
  val catsLib: SettingsDefinition =
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"   % catsV,
      "org.typelevel" %% "cats-kernel" % catsV
    )

  // CatsEffect (Apache2): https://github.com/typelevel/cats-effect
  val catsEffectLib: SettingsDefinition =
    libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectV withSources () withJavadoc ()

  // Decline (Apache2): https://github.com/bkirwi/decline
  val declineLib: SettingsDefinition =
    libraryDependencies ++= Seq(
      "com.monovore" %% "decline"        % declineV,
      "com.monovore" %% "decline-effect" % declineV
    )

  // PPrint
  val pprintLib: SettingsDefinition =
    libraryDependencies += "com.lihaoyi" %% "pprint" % pprintV

  // PureConfig (Mozilla2): https://github.com/pureconfig/pureconfig
  val pureConfigLib: SettingsDefinition =
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig"         % pureConfigV,
      "com.github.pureconfig" %% "pureconfig-core"    % pureConfigV,
      "com.github.pureconfig" %% "pureconfig-generic" % pureConfigV
    )

  // -- Plugins

  // BetterMonadic (MIT): https://github.com/oleg-py/better-monadic-for
  val betterMonadicPlugin: SettingsDefinition =
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicV)

  // Kind-Projector (MIT): https://github.com/typelevel/kind-projector
  val kindProjectorPlugin: SettingsDefinition = Seq(
    //resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full)
  )
}
