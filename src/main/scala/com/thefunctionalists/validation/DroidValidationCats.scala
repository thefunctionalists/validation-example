package com.thefunctionalists.validation.cats

import cats.SemigroupK
import cats.data.Validated._
import cats.data.Validated
import cats.data.ValidatedNel
import cats.data.NonEmptyList
import cats.syntax.cartesian._

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

  val validate: Droid => ValidatedNel[DroidError, Droid] =
    droid => {
      (notEmpty(droid.name)
        |@| notFromDarkSide(droid.owner)
        |@| isOldEnough(droid.age)) map {
          (_, _, _) => droid
        }
    }

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

  def register = validate(Droid()).map(registerDroid)
}

trait DroidService {
  def registerDroid: Droid => Droid = ???
}

object Main extends App with UcRegisterDroid with DroidValidation with DroidService {

  val d = Droid(name = "some name")

  val foo = fromEither(for {
    v <- validate(Droid(age = 2)).toEither
    w <- validate(d).toEither
  } yield w)

  val bar = validate(d)

  val baz = validate(Droid(name = "")).andThen(x => { println("bazzzz"); validate(d) })

  println(s"foo:$foo")
  println(s"bar:$bar")
  println(s"baz:$baz")
}
