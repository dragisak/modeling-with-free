package videostore.zioimpl

import videostore.Logging
import zio.UIO

object StdoutLogging extends Logging[UIO] {
  override def info(msg: String): UIO[Unit]    = UIO(println("INFO: " + msg))
  override def warning(msg: String): UIO[Unit] = UIO(println("WARN: " + msg))
  override def error(msg: String): UIO[Unit]   = UIO(println("ERROR: " + msg))
}
