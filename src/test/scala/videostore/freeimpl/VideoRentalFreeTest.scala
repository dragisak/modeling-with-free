package videostore.freeimpl

import cats.tests.CatsSuite
import videostore.{ErrorOr, VideoRentalTest}
import videostore.freeimpl.VideoRentalFree.Interpreter
import FreeMonadError._

class VideoRentalFreeTest extends CatsSuite with FreeTests {
  override val interpreter: Interpreter[ErrorOr] = InMemoryVideoRental.interpreter

  checkAll("VideoRentalFree", VideoRentalTest(VideoRentalFree[ErrorOr]).allTests)
}
