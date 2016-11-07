package videostore.impl

import java.util.UUID

import videostore._

object InMemory extends VideoStoreInterpreter[ErrorOr] {

  override def interpreter(): VideoRental.Interp[ErrorOr] = new VideoRental.Interp[ErrorOr] {

    private var movies: Map[Movie, Set[DVD]] = Map()
    private var availableDVDs: Set[DVD] = Set()
    private var rentedDVDs: Set[DVD] = Set()


    override def addInventory(movie: Movie, qty: Int): ErrorOr[Set[DVD]] = {
      val dvds = List.fill(qty)(UUID.randomUUID()).toSet
      movies += movie -> (movies.getOrElse(movie, Set()) ++ dvds)
      availableDVDs ++= dvds
      Right(dvds)
    }

    override def searchForDVD(movie: Movie): ErrorOr[Option[DVD]] = {
      Right(movies.get(movie) flatMap (_.headOption))
    }

    override def rentDVD(dvd: DVD): ErrorOr[Unit] = {
      if (availableDVDs.contains(dvd)) {
        availableDVDs -= dvd
        rentedDVDs += dvd
        Right(())
      } else {
        Left(s"$dvd is not available")
      }
    }

    override def returnDVD(dvd: DVD): ErrorOr[Unit] = {
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
