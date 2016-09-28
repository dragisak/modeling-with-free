package videostore

import cats.free.Free
import freasymonad.free


@free trait Logging {

  sealed trait DSL[A]

  type LoggingF[A] = Free[DSL, A]

  def info(msg: String): LoggingF[Unit]

  def warning(msg: String): LoggingF[Unit]

  def error(msg: String): LoggingF[Unit]

}



