package com

import scala.annotation.tailrec
import scala.util.{Failure, Try, Success => SuccessfulTry}

//implement a retry method that accepts a function and a number of attempts; should retry up to that number of
// attempts if function fails, then throw exception
object Retry {


  /**
    *
    * @param attempts - the number of times to attempt retrying this function
    * @param fn       - the function to retry
    * @param success  - Definition of what a "success" is
    * @tparam A - the return type of the function the retry
    * @return - the result of the retry function
    *
    */
  def retry[A](attempts: Int)(fn: () ⇒ A)(implicit success: Success[A]): A = {


    @tailrec
    def retryLoop(attempt: Int, lastFailure: Option[Throwable] = None): A = {
      attempt match {
        case a if a == 0 ⇒ throw RetryFailure(attempts, lastFailure)

        case a ⇒
          Try(fn()) match {
            case SuccessfulTry(result) ⇒

              if (success.isSuccessful(result))
                result
              else
                retryLoop(a - 1)


            case Failure(e) ⇒ retryLoop(a - 1, Some(e))
          }
      }
    }

    retryLoop(attempts)
  }


  /**
    *
    * @param isSuccessful - definition of what "success" means
    * @tparam A - the input type
    */
  case class Success[A](isSuccessful: A ⇒ Boolean)


  case class RetryFailure(attempts: Int, exception: Option[Throwable] = None, message: String) extends RuntimeException(message)

  object RetryFailure {
    def apply(attempts: Int, exception: Option[Throwable]): RetryFailure = {
      val msg = s"Retry failed after $attempts attempts. "
      val exceptionMsg = s"${exception.map(e ⇒ s"The following exception was thrown on the last attempt: ${e.getMessage} ").getOrElse("")}"

      RetryFailure(attempts, exception, msg + exceptionMsg)
    }
  }

}
