package com.thefunctionalists.validation

import org.specs2.mutable.Specification
import scalaz.NonEmptyList

class DroidValidationTest
    extends Specification
    with DroidValidation {

  "Droid validation" should {
    "succeed if all fields ok" >> {
      validate(Droid()).toEither should beRight
    }

    "fail on empty name" >> {
      validate(Droid(name = "")).toEither should beLeft(NonEmptyList(EmptyFieldError("name")))
    }

    "fail if droid from dark side is" >> {
      validate(Droid(owner = "Vader")).toEither should beLeft(NonEmptyList(FromDarkSideError))
    }

    "fail if droid is too young" >> {
      validate(Droid(age = 2)).toEither should beLeft(NonEmptyList(TooYoungError))
    }

  }

}
