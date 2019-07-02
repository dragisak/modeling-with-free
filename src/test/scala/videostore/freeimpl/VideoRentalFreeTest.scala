package videostore.freeimpl

import cats.implicits._
import cats.tests.CatsSuite
import cats.~>
import videostore.freeimpl.Combined.Program
import videostore.{ErrorOr, FreeTests, VideoRentalTest}

class VideoRentalFreeTest extends CatsSuite with FreeTests {
  checkAll("VideoRentalFree", VideoRentalTest(VideoRentalFree[Combined.Program]).allTests)

}
