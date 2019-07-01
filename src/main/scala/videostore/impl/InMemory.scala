package videostore.impl

import java.util.UUID

import videostore.{DVD, ErrorOr, Movie}
import videostore.VideoRental._

object InMemory {

  private var movies: Map[Movie, Set[DVD]] = Map()
  private var availableDVDs: Set[DVD]      = Set()
  private var rentedDVDs: Set[DVD]         = Set()

  val interpreter: Interpreter[ErrorOr] = new Interpreter[ErrorOr] {

    override def apply[A](fa: DSL[A]): ErrorOr[A] = fa match {

      case AddInventory(movie, qty) =>
        val dvds = List.fill(qty)(UUID.randomUUID()).toSet
        movies += movie -> (movies.getOrElse(movie, Set()) ++ dvds)
        availableDVDs ++= dvds
        Right(dvds)

      case SearchForDVD(movie) =>
        Right(movies.get(movie).flatMap(_.headOption))

      case RentDVD(dvd) =>
        if (availableDVDs.contains(dvd)) {
          availableDVDs -= dvd
          rentedDVDs += dvd
          Right(())
        } else {
          Left(s"$dvd is not available")
        }

      case ReturnDVD(dvd) =>
        if (rentedDVDs.contains(dvd)) {
          rentedDVDs -= dvd
          availableDVDs += dvd
          Right(())
        } else {
          Left(s"$dvd has not been rented")
        }
    }
  }
}
