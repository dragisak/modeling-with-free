package videostore

import cats.free.Free
import cats.{InjectK, ~>}
import VideoRental._

object VideoRental {

  sealed trait DSL[A]

  type Interpreter[F[_]] = DSL ~> F

  final case class AddInventory(movie: Movie, qty: Int) extends DSL[Set[DVD]]
  final case class SearchForDVD(movie: Movie)           extends DSL[Option[DVD]]
  final case class RentDVD(dvd: DVD)                    extends DSL[Unit]
  final case class ReturnDVD(dvd: DVD)                  extends DSL[Unit]

  implicit def apply[F[_]](implicit I: InjectK[DSL, F]): VideoRental[F] = new VideoRental[F]

}

class VideoRental[F[_]](implicit I: InjectK[DSL, F]) {
  def addInventory(movie: Movie, qty: Int): Free[F, Set[DVD]] = Free.inject(AddInventory(movie, qty))
  def searchForDVD(movie: Movie): Free[F, Option[DVD]]        = Free.inject(SearchForDVD(movie))
  def rentDVD(dvd: DVD): Free[F, Unit]                        = Free.inject(RentDVD(dvd))
  def returnDVD(dvd: DVD): Free[F, Unit]                      = Free.inject(ReturnDVD(dvd))
}
