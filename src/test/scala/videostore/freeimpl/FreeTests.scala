package videostore.freeimpl

import cats.arrow.FunctionK
import cats.free._
import cats.implicits._
import cats.{Eq, InjectK}
import videostore.ErrorOr
import videostore.freeimpl.VideoRentalFree.DSL

trait FreeTests {

  def interpreter: VideoRentalFree.Interpreter[ErrorOr]

  implicit val inject: InjectK[DSL, ErrorOr] = new InjectK[DSL, ErrorOr] {
    override def inj: FunctionK[DSL, ErrorOr] = interpreter

    override def prj: FunctionK[ErrorOr, Lambda[A => Option[DSL[A]]]] =
      λ[FunctionK[ErrorOr, Lambda[A => Option[DSL[A]]]]] {
        case Left(err) => err
        case Right(a)  => None
      }
  }

  implicit def freeDSLEq[A: Eq]: Eq[Free[DSL, A]] =
    Eq.by(_.foldMap(interpreter))

  implicit def freeErrorOrEq[A: Eq]: Eq[Free[ErrorOr, A]] =
    Eq.by(_.foldMap(FunctionK.id))

}
