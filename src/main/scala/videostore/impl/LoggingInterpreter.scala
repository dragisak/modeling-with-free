package videostore.impl

import videostore.Logging

trait LoggingInterpreter[F[_]] {
  def interpreter(): Logging.Interp[F]

}
