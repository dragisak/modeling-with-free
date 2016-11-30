package videostore

import cats.free.Free
import freasymonad.cats.free


@free trait VideoRental {

  sealed trait DSL[A]

  type VideoRentalF[A] = Free[DSL, A]

  def addInventory(movie: Movie, qty: Int): VideoRentalF[Set[DVD]]

  def searchForDVD(movie: Movie): VideoRentalF[Option[DVD]]

  def rentDVD(dvd: DVD): VideoRentalF[Unit]

  def returnDVD(dvd: DVD): VideoRentalF[Unit]

}



trait VideoStoreInterpreter[F[_]] {

  def apply(): VideoRental.Interp[F]

}
