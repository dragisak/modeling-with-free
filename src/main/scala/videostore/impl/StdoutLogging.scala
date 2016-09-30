package videostore.impl

import cats.Id
import videostore.Logging.Interp
import videostore._

object StdoutLogging extends LoggingInterpreter[Id] {
  override def interpreter(): Interp[Id] = new Logging.Interp[Id] {
    override def info(msg: String): Id[Unit] = println("INFO " + msg)

    override def warning(msg: String): Id[Unit] = println("WARN " + msg)

    override def error(msg: String): Id[Unit] = println("ERROR: " + msg)
  }
}