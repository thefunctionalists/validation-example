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


trait UcRegisterDroid {
  self: DroidValidation with DroidService =>

  //the simplest scenario, for more complex use for comprehension
  def register = validate(Droid()).map(registerDroid)
}

trait DroidService {
  def registerDroid: Droid => Unit = ???
}
