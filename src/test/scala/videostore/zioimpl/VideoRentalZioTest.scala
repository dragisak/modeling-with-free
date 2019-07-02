package videostore.zioimpl

import cats.tests.CatsSuite
import videostore.VideoRentalTest
import zio.interop.catz._

class VideoRentalZioTest extends CatsSuite with ZioTests {
  checkAll("VideoRentalFree", VideoRentalTest(InMemoryVideoRental).allTests)
}
