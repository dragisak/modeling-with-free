package videostore.freeimpl

import cats.tests.CatsSuite
import videostore.VideoRentalTest

class VideoRentalFreeTest extends CatsSuite with FreeTests {
  checkAll("VideoRentalFree", VideoRentalTest(VideoRentalFree[Program]).allTests)
}
