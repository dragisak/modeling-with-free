package videostore

import freek._

package object impl {
  def interpreter(): Interpreter[PRG.Cop, ErrorOr] = StdoutLogging.interpreter().interpreter :&: InMemory.interpreter().interpreter

}
