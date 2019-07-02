package videostore

import zio.IO

package object zioimpl {
  type TaskOrError[A] = IO[Error, A]
}
