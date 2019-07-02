package videostore.freeimpl

import cats.tests.CatsSuite
import videostore.{ErrorOr, VideoRentalTest}
import videostore.freeimpl.VideoRentalFree.{DSL, Interpreter}

class VideoRentalFreeTest extends CatsSuite with FreeTests {
  override val interpreter: Interpreter[ErrorOr] = InMemoryVideoRental.interpreter

  // checkAll("VideoRentalFree", VideoRentalTest(VideoRentalFree[DSL]).allTests)
}
