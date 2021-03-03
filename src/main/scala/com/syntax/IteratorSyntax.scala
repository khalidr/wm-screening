package com.syntax

trait IteratorSyntax {

  implicit class RichIterator[A](var iterator: Iterator[A]) {

    //implement an iterator wrapper which takes an iterator and adds a peek() method which doesn’t advance the pointer
    def peekOption(): Option[A] = {
      val buffered = iterator.buffered
      iterator = buffered
      buffered.headOption
    }

    def peek(): A = {
      val buffered = iterator.buffered
      iterator = buffered
      buffered.head
    }
  }
}

object iterator extends IteratorSyntax
