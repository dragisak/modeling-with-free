package videostore.impl

import java.util.UUID

import cats._
import cats.data.Xor
import videostore._

object InMemory {
  import videostore.VideoRental._

  def inMemory: VideoRental.DSL ~> ErrorOr = new (DSL ~> ErrorOr) {

    private var movies: Map[Movie, Set[DVD]] = Map()
    private var availableDVDs: Set[DVD] = Set()
    private var rentedDVDs: Set[DVD] = Set()

    override def apply[A](fa: DSL[A]): ErrorOr[A] = fa match {

      case AddInventory(movie, qty) =>
        val dvds = List.fill(qty)(UUID.randomUUID()).toSet
        movies += movie -> (movies.getOrElse(movie, Set()) ++ dvds)
        availableDVDs ++= dvds
        Xor.Right(dvds)

      case SearchForDVD(movie) =>
        Xor.Right(movies.get(movie) flatMap (_.headOption))

      case RentDVD(dvd) =>
        if(availableDVDs.contains(dvd)) {
          availableDVDs -= dvd
          rentedDVDs += dvd
          Xor.Right(())
        } else {
          Xor.Left(s"$dvd is not available")
        }


      case ReturnDVD(dvd) =>
        if(rentedDVDs.contains(dvd)) {
          rentedDVDs -= dvd
          availableDVDs += dvd
          Xor.Right(())
        } else {
          Xor.Left(s"$dvd has not been rented")
        }

    }
  }

}
