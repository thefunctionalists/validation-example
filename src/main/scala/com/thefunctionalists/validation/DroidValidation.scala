package com.thefunctionalists.validation

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
  def validate: Droid => ValidationNel[DroidError, Droid] =
    droid =>
      (notEmpty(droid.name)
        |@| notFromDarkSide(droid.owner)
        |@| isOldEnough(droid.age)) {
          (_, _, _) => droid
        }

  private def notEmpty(name: String): ValidationNel[DroidError, String] =
    if (name.isEmpty) EmptyFieldError("name").failureNel
    else name.successNel

  private def notFromDarkSide(owner: String): ValidationNel[DroidError, String] =
    if (owner == "Darth" || owner == "Vader") FromDarkSideError.failureNel
    else owner.successNel

  private def isOldEnough(age: Long): ValidationNel[DroidError, Long] =
    if (age > 3) age.successNel
    else TooYoungError.failureNel
}

trait UcRegisterDroid {
  self: DroidValidation with DroidService =>

  //the simplest scenario, for more complex use for comprehension
  def register = validate(Droid()).map(registerDroid)
}

trait DroidService {
  def registerDroid: Droid => Unit = ???
}
