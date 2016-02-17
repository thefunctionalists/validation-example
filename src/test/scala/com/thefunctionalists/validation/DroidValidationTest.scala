package com.thefunctionalists.validation

import org.specs2.mutable.Specification
import scalaz.NonEmptyList

class DroidValidationTest
    extends Specification
    with DroidValidation {

  "Droid validation" should {

    "fail on empty name" >> {
      validate(Droid(name = "")).toEither should beLeft(NonEmptyList(EmptyFieldError("name"), EmptyFieldError("name")))
    }

  }

}
