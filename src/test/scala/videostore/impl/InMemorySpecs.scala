package videostore.impl

import videostore.VideoRentalRules

class InMemorySpecs extends VideoRentalRules(InMemory.inMemory)
