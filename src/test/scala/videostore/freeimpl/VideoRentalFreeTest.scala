package videostore.freeimpl

import cats.tests.CatsSuite
import videostore.VideoRentalTest
import videostore.freeimpl.VideoRentalFree.DSL

class VideoRentalFreeTest extends CatsSuite with FreeTests {
  checkAll("VideoRentalFree", VideoRentalTest(VideoRentalFree[DSL]).allTests)
}
