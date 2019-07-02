package videostore

trait VideoRental[F[_]] {
  def addInventory(movie: Movie, qty: Int): F[Set[DVD]]
  def searchForDVD(movie: Movie): F[Option[DVD]]
  def rentDVD(dvd: DVD): F[Unit]
  def returnDVD(dvd: DVD): F[Unit]
}
