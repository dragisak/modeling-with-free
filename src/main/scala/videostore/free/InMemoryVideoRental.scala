package videostore.free

import java.util.UUID
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import videostore.{DVD, ErrorOr, Movie}
import VideoRentalFree._
import scala.collection.JavaConverters._

object InMemoryVideoRental {

  private val movies: ConcurrentMap[Movie, Set[DVD]]  = new ConcurrentHashMap[Movie, Set[DVD]]() {}
  private val availableDVDs: ConcurrentMap[DVD, Unit] = new ConcurrentHashMap[DVD, Unit]()
  private val rentedDVDs: ConcurrentMap[DVD, Unit]    = new ConcurrentHashMap[DVD, Unit]()

  val interpreter: Interpreter[ErrorOr] = new Interpreter[ErrorOr] {

    override def apply[A](fa: DSL[A]): ErrorOr[A] = fa match {

      case AddInventory(movie, qty) =>
        val dvds = List.fill(qty)(UUID.randomUUID())

        val current = movies.getOrDefault(movie, Set[DVD]()) ++ dvds

        movies.put(movie, current)

        availableDVDs.putAll(dvds.map((_, ())).toMap.asJava)
        Right(dvds.toSet)

      case SearchForDVD(movie) =>
        Right(Option(movies.get(movie)).flatMap(_.headOption))

      case RentDVD(dvd) =>
        if (availableDVDs.containsKey(dvd)) {
          availableDVDs.remove(dvd)
          rentedDVDs.put(dvd, ())
          Right(())
        } else {
          Left(s"$dvd is not available")
        }

      case ReturnDVD(dvd) =>
        if (rentedDVDs.containsKey(dvd)) {
          rentedDVDs.remove(dvd)
          availableDVDs.put(dvd, ())
          Right(())
        } else {
          Left(s"$dvd has not been rented")
        }
    }
  }
}
