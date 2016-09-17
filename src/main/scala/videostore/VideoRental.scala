package videostore

import cats.free.Free
import cats.free.Free._

sealed trait VideoRental[A]

case class AddInventory[Movie, DVD](movie: Movie, qty: Int) extends VideoRental[Set[DVD]]

case class SearchForDVD[Movie, DVD](movie: Movie) extends VideoRental[Option[DVD]]

case class RentDVD[DVD](dvd: DVD) extends VideoRental[Unit]

case class ReturnDVD[DVD](dvd: DVD) extends VideoRental[Unit]

object VideoRental {

  type VideoRentalF[A] = Free[VideoRental, A]

  def addInventory[Movie, DVD](movie: Movie, qty: Int): VideoRentalF[Set[DVD]] = {
    liftF[VideoRental, Set[DVD]](AddInventory(movie, qty))
  }

  def searchForDVD[Movie, DVD](movie: Movie): VideoRentalF[Option[DVD]] = {
    liftF[VideoRental, Option[DVD]](SearchForDVD(movie))
  }

  def rentDVD[DVD](dvd: DVD): VideoRentalF[Unit] = {
    liftF[VideoRental, Unit](RentDVD(dvd))
  }

  def returnDVD[DVD](dvd: DVD): VideoRentalF[Unit] = {
    liftF[VideoRental, Unit](ReturnDVD(dvd))
  }


}



