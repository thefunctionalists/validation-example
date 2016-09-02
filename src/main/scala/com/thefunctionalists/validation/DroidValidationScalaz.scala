package com.thefunctionalists.validation.scalaz

import scalaz._
import Scalaz._

case class Droid(
  name: String = "R2D2",
  programmingLang: String = "Scala",
  spokenLang: String = "bytecode",
  owner: String = "Luke",
  age: Long = 10,
  height: Long = 109
)

sealed trait DroidError
case class EmptyFieldError(field: String) extends DroidError
case object FromDarkSideError extends DroidError
case object TooYoungError extends DroidError

trait DroidValidation {
  val validate: Droid => ValidationNel[DroidError, Droid] =
    droid =>
      (notEmpty(droid.name).toValidationNel
        |@| notFromDarkSide(droid.owner).toValidationNel
        |@| isOldEnough(droid.age).toValidationNel) {
          (_, _, _) => droid
        }

  private def notEmpty(name: String): Validation[DroidError, String] =
    if (name.isEmpty) EmptyFieldError("name").failure
    else name.success

  private def notFromDarkSide(owner: String): Validation[DroidError, String] =
    if (owner == "Darth" || owner == "Vader") FromDarkSideError.failure
    else owner.success

  private def isOldEnough(age: Long): Validation[DroidError, Long] =
    if (age > 3) age.success
    else TooYoungError.failure
}

trait UcRegisterDroid {
  self: DroidValidation with DroidService =>

  def register = validate(Droid()).map(registerDroid)
}

trait DroidService {
  def registerDroid: Droid => Droid = ???
}
