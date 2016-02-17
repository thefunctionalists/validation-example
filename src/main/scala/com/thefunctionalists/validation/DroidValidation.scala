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

trait SomeOuterCode {
  validation: DroidValidation =>

  def registerDroid = validate(Droid())

}

trait DroidValidation {

  def validate: Droid => ValidationNel[DroidError, Droid] =
    droid =>
      (notEmpty(droid.name) |@| notEmpty(droid.name)) {
        (_, _) => droid
      }

  private def notEmpty(name: String): ValidationNel[DroidError, String] =
    if (name.isEmpty) EmptyFieldError("name").failureNel
    else name.successNel

}

sealed trait DroidError
case class EmptyFieldError(field: String) extends DroidError
