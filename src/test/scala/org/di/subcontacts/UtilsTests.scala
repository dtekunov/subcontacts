package org.di.subcontacts

import org.di.subcontacts.ContactsServiceProtocol.formatResponse
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpecLike

class UtilsTests extends AnyWordSpecLike {

  "formatter of the response" must {
    "format the output correctly for an empty Set" in {
      val toTest = Set.empty[Int]
      val result = "[]"

      formatResponse(toTest) shouldBe result
    }

    "format the output correctly for single-element Set" in {
      val toTest = Set(1)
      val result = "[1]"

      formatResponse(toTest) shouldBe result
    }

    "format the output correctly for multi-elements Set" in {
      val toTest = Set(1,2,3,4)
      val result = "[1, 2, 3, 4]"

      formatResponse(toTest) shouldBe result
    }
  }

}
