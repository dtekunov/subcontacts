package org.di.subcontacts

import org.di.subcontacts.logic.LookupOperations
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.concurrent.TrieMap

class LookupOperationsTests extends LookupOperations with AnyWordSpecLike {
  val testContacts = TrieMap(
    1 -> Array(2,3,4,5),
    2 -> Array(1,6,8),
    3 -> Array(1,4),
    4 -> Array(1,3),
    5 -> Array(1,6)
  )

  "lookup operations trait" must {

    "correctly get contacts" in {
      val toTest = 1
      val result = Set(2,3,4,5)

      getContacts(toTest, testContacts) shouldBe result
    }

    "return empty set for non-existing value" in {
      val toTest = 13
      val result = Set.empty[Int]

      getContacts(toTest, testContacts) shouldBe result
    }

    "return valid sub-contacts without repetitions" in {
      val toTest = 1
      val result = Set(1,6,8,4,3)

      getSubContacts(getContacts(toTest, testContacts), testContacts) shouldBe result
    }

    "correctly get second-degree contacts" in {
      val toTest = 1
      val result = Set(6,8)

      getSecondDegreeContacts(toTest, testContacts) shouldBe result
    }

    "correctly get second-degree contacts with non-existing contacts" in {
      val toTest = 2
      val result = Set(3,4,5)

      getSecondDegreeContacts(toTest, testContacts) shouldBe result
    }
  }
}
