package videostore.impl

import videostore.VideoRentalRules

class InMemorySpecs extends VideoRentalRules {

  override def interpreter = new InMemory
}
