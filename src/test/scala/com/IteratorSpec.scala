package com

import iteratorSyntax._

class IteratorSpec extends UnitSpec {

  "iterator" should "have a peek method that does not advance the pointer" in {
    val i = Iterator(1, 2, 3, 4)

    i.peek() shouldBe 1

    i.next() shouldBe 1

    i.peekOption().value shouldBe 2

    i.next() shouldBe 2
  }
}
