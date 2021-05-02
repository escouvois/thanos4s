import cats.effect.Sync
import fansi.Bold
import fansi.Color.LightYellow
import fansi.Color.White

package object thanos {

  object syntax {

    implicit final class StringOps(val s: String) extends AnyVal {

      def tab: String = {
        val LINE = LightYellow ++ Bold.On
        val TEXT = White ++ Bold.On
        val len  = if (s.split("\n").size > 1) s.split("\n")(0).size else s.size

        s"""${LINE(s"${"_" * (2 + len)}")}
           |${s.split("\n").map(s => TEXT(s" $s ")).mkString(s"${LINE("#")}", s"\n${LINE("#")}", "")}""".stripMargin
      }
    }
  }

  implicit final class IoOps[A, F[_]: Sync](a: => A) {
    def io: F[A] = Sync[F].delay(a)
  }

  implicit final class PrintOps[F[_]: Sync](a: => Any) {
    def printIO:  F[Unit] = Predef.println(a).io
    def pprintIO: F[Unit] = pprint.pprintln(a).io
  }
}
