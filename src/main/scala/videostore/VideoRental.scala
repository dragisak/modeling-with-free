package videostore

import cats.free.Free
import cats.free.Free._


object VideoRental {

  sealed trait DSL[A]

  final case class AddInventory(movie: Movie, qty: Int) extends DSL[Set[DVD]]

  final case class SearchForDVD(movie: Movie) extends DSL[Option[DVD]]

  final case class RentDVD(dvd: DVD) extends DSL[Unit]

  final case class ReturnDVD(dvd: DVD) extends DSL[Unit]


  type VideoRentalF[A] = Free[DSL, A]

  def addInventory(movie: Movie, qty: Int): VideoRentalF[Set[DVD]] = {
    liftF[DSL, Set[DVD]](AddInventory(movie, qty))
  }

  def searchForDVD(movie: Movie): VideoRentalF[Option[DVD]] = {
    liftF[DSL, Option[DVD]](SearchForDVD(movie))
  }

  def rentDVD(dvd: DVD): VideoRentalF[Unit] = {
    liftF[DSL, Unit](RentDVD(dvd))
  }

  def returnDVD(dvd: DVD): VideoRentalF[Unit] = {
    liftF[DSL, Unit](ReturnDVD(dvd))
  }


}



