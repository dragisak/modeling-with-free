package videostore

import cats.MonadError
import cats.implicits._
import cats.laws._
import org.typelevel.discipline.Laws

trait VideoRentalLaws[F[_]] extends Laws {

  implicit def M: MonadError[F, Error]

  def videoRental: VideoRental[F]

  def addInventory(movie: Movie, qty: Int) = {
    val op = videoRental.addInventory(movie, qty)
    op.map(_.size) <-> M.pure(qty)
  }

  def rentDvd(movie: Movie, qty: Int) = {
    val op = for {
      dvds <- videoRental.addInventory(movie, qty)
      dvd  = dvds.head
      _    <- videoRental.rentDVD(dvd)
    } yield ()

    op <-> M.pure(())
  }

  def returnRentedDvd(movie: Movie, qty: Int) = {
    val op = for {
      dvds <- videoRental.addInventory(movie, qty)
      dvd  = dvds.head
      _    <- videoRental.rentDVD(dvd)
      _    <- videoRental.returnDVD(dvd)
    } yield ()
    op <-> M.pure(())
  }

  def preventFromRentingSameDvdTwice(movie: Movie, qty: Int) = {
    val op = for {
      dvds <- videoRental.addInventory(movie, qty)
      dvd  = dvds.head
      _    <- videoRental.rentDVD(dvd)
      _    <- videoRental.rentDVD(dvd)
    } yield false

    op.handleError(_.nonEmpty) <-> M.pure(true)
  }

  def findIfDvdIsAvailable(movie: Movie, qty: Int) = {
    val op = for {
      _   <- videoRental.addInventory(movie, qty)
      res <- videoRental.searchForDVD(movie)
    } yield res

    op.map(_.isDefined) <-> M.pure(true)
  }

}

object VideoRentalLaws {
  def apply[F[_]](instance: VideoRental[F])(implicit ev: MonadError[F, Error]): VideoRentalLaws[F] =
    new VideoRentalLaws[F] {
      override implicit val M: MonadError[F, Error] = ev
      override val videoRental: VideoRental[F]      = instance
    }
}
