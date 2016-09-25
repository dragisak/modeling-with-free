package videostore.impl

import videostore.VideoRental

trait VideoStoreInterpreter[F[_]] {

  def interpreter(): VideoRental.Interp[F]

}
