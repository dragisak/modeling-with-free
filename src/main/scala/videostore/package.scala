import cats.data.{OptionT, Xor}

package object videostore {
  type Error = String

  type Response[A] = Xor[Error, A]
  type OptResponse[A] = OptionT[Response, A]
}
