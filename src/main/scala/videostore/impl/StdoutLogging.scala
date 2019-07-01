package videostore.impl

import cats.Id

import videostore.Logging._

object StdoutLogging {

  val interpreter: Interpreter[Id] = new Interpreter[Id] {

    override def apply[A](fa: DSL[A]): Id[A] = fa match {
      case Info(msg)    => println("INFO: " + msg)
      case Warning(msg) => println("WARN: " + msg)
      case Error(msg)   => println("ERROR: " + msg)
    }
  }
}
