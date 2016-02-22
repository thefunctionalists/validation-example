package com.thefunctionalists.validation.cats

import cats.SemigroupK
import cats.data.Validated._
import cats.data.Validated
import cats.data.ValidatedNel
import cats.data.NonEmptyList
import cats.syntax.cartesian._
import cats.std.list._

case class Droid(
  name: String = "R2D2",
  programmingLang: String = "Scala",
  spokenLang: String = "bytecode",
  owner: String = "Luke",
  age: Long = 10,
  height: Long = 109
)

sealed abstract class DroidError
final case class EmptyFieldError(field: String) extends DroidError
final case object FromDarkSideError extends DroidError
final case object TooYoungError extends DroidError

trait DroidValidation {

  implicit val nelSemigroup = SemigroupK[NonEmptyList].algebra[DroidError]

  def validate: Droid => ValidatedNel[DroidError, Droid] =
    droid => {
      (notEmpty(droid.name)
        |@| notFromDarkSide(droid.owner)
        |@| isOldEnough(droid.age)) map {
          (_, _, _) => droid
        }
    }

  //can be moved as generic validator, but then Error shoud be also generalized
  private def notEmpty(name: String): ValidatedNel[DroidError, String] =
    if (name.isEmpty) invalidNel(EmptyFieldError("name"))
    else valid(name)

  private def notFromDarkSide(owner: String): ValidatedNel[DroidError, String] =
    if (owner == "Darth" || owner == "Vader") invalidNel(FromDarkSideError)
    else valid(owner)

  private def isOldEnough(age: Long): ValidatedNel[DroidError, Long] =
    if (age > 3) valid(age)
    else invalidNel(TooYoungError)
}

trait UcRegisterDroid {
  self: DroidValidation with DroidService =>

  //the simplest scenario, for more complex use for comprehension
  // and maybe change return type to other than validation
  def register = validate(Droid()).map(registerDroid)
}

trait DroidService {
  def registerDroid: Droid => Droid = ???
}
