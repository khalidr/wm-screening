package com

import java.util.concurrent.atomic.AtomicInteger

import com.Retry._


class RetrySpec extends UnitSpec {

  "retry" should "retry a function n times" in {

    implicit val success: Success[Int] = Success[Int](_ == 5)

    val i = new AtomicInteger()

    retry(5) {() ⇒
      i.incrementAndGet()
    }

    i.get() shouldBe 5
  }

  it should "throw an exception when retries have been exhausted without a success" in {
    implicit val success: Success[Int] = Success[Int](_ == 100)

    val i = new AtomicInteger()
    val numberOfAttempts = 5

    val caughtException = intercept[RetryFailure]{
      retry(numberOfAttempts) {() ⇒
        i.incrementAndGet()
      }
    }

    caughtException.attempts shouldBe numberOfAttempts
    caughtException.exception shouldBe empty

    i.get() shouldBe 5

  }

  it should "throw an exception containing a root cause exception when the retry function throws an " +
    "exception on the last attempt" in {

    implicit val success: Success[Int] = Success[Int](_ == 5)

    val i = new AtomicInteger()
    val numberOfAttempts = 5

    val caughtException = intercept[RetryFailure]{
      retry(numberOfAttempts) {() ⇒
        i.incrementAndGet()
        throw SomeException
      }
    }

    caughtException.attempts shouldBe numberOfAttempts
    caughtException.exception.value shouldBe SomeException

    i.get() shouldBe numberOfAttempts
  }

  case object SomeException extends RuntimeException("Whoops!!")
}
