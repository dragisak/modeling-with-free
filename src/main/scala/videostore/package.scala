import java.util.UUID

package object videostore {

  type Movie = String

  type DVD = UUID

  type Error = String

  type ErrorOr[A] = Either[Error, A]

}
