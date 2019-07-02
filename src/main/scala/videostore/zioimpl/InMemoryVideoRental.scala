package videostore.zioimpl

import java.util.UUID
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import videostore.{DVD, Movie, VideoRental}
import zio.IO

import scala.collection.JavaConverters._

object InMemoryVideoRental extends VideoRental[TaskOrError] {

  private val movies: ConcurrentMap[Movie, Set[DVD]]  = new ConcurrentHashMap[Movie, Set[DVD]]() {}
  private val availableDVDs: ConcurrentMap[DVD, Unit] = new ConcurrentHashMap[DVD, Unit]()
  private val rentedDVDs: ConcurrentMap[DVD, Unit]    = new ConcurrentHashMap[DVD, Unit]()

  override def addInventory(movie: Movie, qty: Int): TaskOrError[Set[DVD]] = IO.succeedLazy {
    val dvds = List.fill(qty)(UUID.randomUUID())

    val current = movies.getOrDefault(movie, Set[DVD]()) ++ dvds

    movies.put(movie, current)

    availableDVDs.putAll(dvds.map((_, ())).toMap.asJava)
    dvds.toSet
  }

  override def searchForDVD(movie: Movie): TaskOrError[Option[DVD]] = IO.succeedLazy(
    Option(movies.get(movie)).flatMap(_.headOption)
  )

  override def rentDVD(dvd: DVD): TaskOrError[Unit] =
    for {
      b <- IO.succeedLazy(availableDVDs.containsKey(dvd))
      _ <- if (b) {
            IO.succeedLazy {
              availableDVDs.remove(dvd)
              rentedDVDs.put(dvd, ())
            }
          } else {
            IO.fail(s"$dvd is not available")
          }
    } yield ()

  override def returnDVD(dvd: DVD): TaskOrError[Unit] =
    for {
      b <- IO.succeedLazy(rentedDVDs.containsKey(dvd))
      _ <- if (b) {
            IO.succeedLazy {
              rentedDVDs.remove(dvd)
              availableDVDs.put(dvd, ())
            }
          } else {
            IO.fail(s"$dvd has not been rented")
          }
    } yield ()
}
