package videostore.free

import cats.free.Free
import cats.{InjectK, ~>}
import LoggingFree._
import videostore.Logging

object LoggingFree {

  sealed trait DSL[A]

  type Interpreter[F[_]] = DSL ~> F

  final case class Info(msg: String)    extends DSL[Unit]
  final case class Warning(msg: String) extends DSL[Unit]
  final case class Error(msg: String)   extends DSL[Unit]

  implicit def apply[F[_]](implicit I: InjectK[DSL, F]): LoggingFree[F] = new LoggingFree[F]

}

class LoggingFree[F[_]](implicit I: InjectK[DSL, F]) extends Logging[Free[F, ?]] {
  override def info(msg: String): Free[F, Unit]    = Free.inject(Info(msg))
  override def warning(msg: String): Free[F, Unit] = Free.inject(Warning(msg))
  override def error(msg: String): Free[F, Unit]   = Free.inject(Error(msg))
}
