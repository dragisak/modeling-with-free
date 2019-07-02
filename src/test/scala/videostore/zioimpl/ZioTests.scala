package videostore.zioimpl

import cats.Eq
import videostore.Error
import zio.{DefaultRuntime, IO, Task}

trait ZioTests extends DefaultRuntime {
  implicit def taskEq[A: Eq]: Eq[Task[A]]    = Eq.by(unsafeRun)
  implicit def ioEq[A: Eq]: Eq[IO[Error, A]] = Eq.by(unsafeRun)
}
