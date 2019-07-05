package videostore.doobieimpl

import cats.implicits._
import doobie._
import doobie.implicits._
import videostore.doobieimpl.VideoRentalDoobie.DVDStatus
import videostore.{DVD, Movie, VideoRental}
import doobie.h2.implicits._

class VideoRentalDoobie extends VideoRental[ConnectionIO] {

  override def addInventory(movie: Movie, qty: Int): ConnectionIO[Set[DVD]] =
    if (qty > 0) {
      val dvds = List.fill(qty)(DVD.create)
      val sql  = "insert into dvds (movie, dvd, status) values (?, ?, ?)"
      Update[(Movie, DVD, String)](sql)
        .updateMany(dvds.map(dvd => (movie, dvd, DVDStatus.toString(DVDStatus.Available))))
        .map(_ => dvds.toSet)
    } else {
      Set.empty.pure[ConnectionIO]
    }

  override def searchForDVD(movie: Movie): ConnectionIO[Option[DVD]] =
    sql"select dvd from dvds where movie = $movie and status = 'available' limit 1".query[DVD].option

  override def rentDVD(dvd: DVD): ConnectionIO[Unit] =
    for {
      maybeDvd <- sql"select dvd from dvds where dvd = $dvd and status = 'available' limit 1".query[DVD].option
      _ <- maybeDvd match {
            case Some(availableDvd) => sql"update dvd set status = 'rented' where dvd = $dvd".update
            case None               => s"$dvd is not available".raiseError[ConnectionIO, Option[DVD]]
          }
    } yield ()

  override def returnDVD(dvd: DVD): ConnectionIO[Unit] =
    for {
      maybeDvd <- sql"select dvd from dvds where dvd = $dvd and status = 'rented' limit 1".query[DVD].option
      _ <- maybeDvd match {
            case Some(availableDvd) => sql"update dvd set status = 'available' where dvd = $dvd".update
            case None               => s"$dvd is not rented".raiseError[ConnectionIO, Option[DVD]]
          }
    } yield ()
}

object VideoRentalDoobie {
  sealed trait DVDStatus
  object DVDStatus {
    case object Available extends DVDStatus
    case object Rented    extends DVDStatus

    def toString(st: DVDStatus): String = st match {
      case Available => "available"
      case Rented    => "rented"
    }

    def parse(s: String): Option[DVDStatus] = s match {
      case "available" => Some(Available)
      case "rented"    => Some(Rented)
      case _           => None
    }
  }
}
