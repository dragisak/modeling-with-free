package videostore

import cats.free.Free
import cats.{InjectK, ~>}
import videostore.Logging._

object Logging {

  sealed trait DSL[A]

  type Interpreter[F[_]] = DSL ~> F

  final case class Info(msg: String)    extends DSL[Unit]
  final case class Warning(msg: String) extends DSL[Unit]
  final case class Error(msg: String)   extends DSL[Unit]

  implicit def apply[F[_]](implicit I: InjectK[DSL, F]): Logging[F] = new Logging[F]

}

class Logging[F[_]](implicit I: InjectK[DSL, F]) {
  def info(msg: String): Free[F, Unit]    = Free.inject(Info(msg))
  def warning(msg: String): Free[F, Unit] = Free.inject(Warning(msg))
  def error(msg: String): Free[F, Unit]   = Free.inject(Error(msg))
}
