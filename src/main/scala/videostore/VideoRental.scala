package videostore

import cats.free.Free
import cats.free.Free._

sealed trait VideoRental[A]


case class AddInventory[Movie, DVD](movie: Movie, cnt: Int) extends VideoRental[Response[Set[DVD]]]

case class SearchForDVD[Movie, DVD](movie: Movie) extends VideoRental[OptResponse[DVD]]

case class RentDVD[DVD](dvd: DVD) extends VideoRental[Response[Unit]]

case class ReturnDVD[DVD](dvd: DVD) extends VideoRental[Response[Unit]]

object VideoRental {
  type VideoRentalF[A] = Free[VideoRental, A]

  def addInventory[Movie, DVD](movie: Movie, cnt: Int): VideoRentalF[Response[Set[DVD]]] = {
    liftF[VideoRental, Response[Set[DVD]]](AddInventory(movie, cnt))
  }

  def searchForDVD[Movie, DVD](movie: Movie): VideoRentalF[OptResponse[DVD]] = {
    liftF[VideoRental, OptResponse[DVD]](SearchForDVD(movie))
  }

  def rentDVD[DVD](dvd: DVD): VideoRentalF[Response[Unit]] = {
    liftF[VideoRental, Response[Unit]](RentDVD(dvd))
  }

  def returnDVD[DVD](dvd: DVD): VideoRentalF[Response[Unit]] = {
    liftF[VideoRental, Response[Unit]](ReturnDVD(dvd))
  }


}



