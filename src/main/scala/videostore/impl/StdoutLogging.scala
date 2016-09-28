package videostore.impl

import videostore.Logging.Interp
import videostore._
import cats.syntax.xor._

object StdoutLogging extends LoggingInterpreter[ErrorOr] {
  override def interpreter(): Interp[ErrorOr] = new Logging.Interp[ErrorOr] {
    override def info(msg: String): ErrorOr[Unit] = println("INFO " + msg).right

    override def warning(msg: String): ErrorOr[Unit] = println("WARN " + msg).right

    override def error(msg: String): ErrorOr[Unit] = println("ERROR: " + msg).right
  }
}
