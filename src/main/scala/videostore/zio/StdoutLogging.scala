package videostore.zio

import cats.Id
import videostore.Logging

object StdoutLogging extends Logging[Id] {
  override def info(msg: String): Id[Unit]    = println("INFO: " + msg)
  override def warning(msg: String): Id[Unit] = println("WARN: " + msg)
  override def error(msg: String): Id[Unit]   = println("ERROR: " + msg)
}
