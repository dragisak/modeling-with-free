package videostore

trait Logging[F[_]] {
  def info(msg: String): F[Unit]
  def warning(msg: String): F[Unit]
  def error(msg: String): F[Unit]
}
