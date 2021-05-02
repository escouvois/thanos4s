package thanos

import better.files.File._
import better.files.StringInterpolations
import cats.effect.std.Random
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.Sync
import cats.syntax.all._
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import thanos.syntax._

import java.nio.file.Path

final case class SnappingConfig(
    blacklist:    List[String],
    unleashPower: Boolean
)

final case class SnappingFingers(
    withGloves: Boolean = false,
    universe:   Path,
    verbose:    Boolean = false
)

final case class Thanos[F[_]: Sync](args: SnappingFingers, conf: SnappingConfig) {

  def snap: F[Unit] =
    for {
      _ <- Sync[F].unlessA(args.withGloves) {
             val msg = "There is not point snapping fingers without the Infinity Gauntlet Glove.\nPlease provide --with-gloves flag."
             Sync[F].raiseError(new IllegalArgumentException(msg))
           }
      allFiles <- file"${args.universe}".listRecursively
                    .filter(_.isRegularFile)
                    .filterNot(f => conf.blacklist.contains(f.name))
                    .toList
                    .io
      size = allFiles.size
      _ <- s"Files in directory ${args.universe} (Total Count: $size)".printIO
      shuffled <- Random.scalaUtilRandom >>= (_.shuffleList(allFiles).map(_.take(size / 2)))
      _ <- Sync[F].whenA(args.verbose)(shuffled.pprintIO)
      _ <- Sync[F].whenA(conf.unleashPower)(shuffled.traverse_(_.delete().io))
    } yield ()
}

object ThanosApp
    extends CommandIOApp(
      name = "thanos",
      header = "Thanos will wipe out half of your files",
      version = "0.0.1"
    ) {

  val snapFingersOpts: Opts[SnappingFingers] =
    Opts.subcommand("snap-fingers", "Required to delete your files.") {
      val gloves: Opts[Boolean] = Opts
        .flag(
          "with-gloves",
          "There is not point snapping fingers without the Infinity Gauntlet Glove",
          "g"
        )
        .orFalse
      val universe: Opts[Path] =
        Opts
          .option[Path]("universe", "Universe to snap. Without providing it, current directory is selected", "u", "directory")
          .withDefault(currentWorkingDirectory.path)
          .validate("<directory> must be at least two levels underneath the root directory")(path => !root.list.contains(file"$path"))
      val verbose: Opts[Boolean] = Opts.flag("verbose", "Verbose mode", "v").orFalse
      (gloves, universe, verbose).mapN(SnappingFingers)
    }

  override def main: Opts[IO[ExitCode]] =
    snapFingersOpts.map(Program.run[IO](_).as(ExitCode.Success))
}

object Program {

  def run[F[_]: Sync](args: SnappingFingers): F[Unit] =
    Sync[F].handleErrorWith(
      ConfigSource
        .resources("thanos.conf")
        .loadOrThrow[SnappingConfig]
        .io
        .flatMap(conf => s"Run with args: ${pprint.apply(args)}".printIO *> Thanos[F](args, conf).snap)
    )(_.getMessage.tab.printIO)
}
