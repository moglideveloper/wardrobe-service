package com

import cats.data._
import cats.implicits._

import scala.util.Try

package object mindera {

  case class PropertiesMap(private val map : Map[String, String]){
    def apply(key : String): Option[String] = {
      println(s"attempt to read >$key< from properties")
      map.get(key)
    }
  }

  case class EnvMap(private val map : Map[String, String]){
    def apply(key : String): Option[String] = {
      println(s"attempt to read >$key< from system env")
      map.get(key)
    }
  }

  case class ServiceError(
      responseMsg: String,
      mayBeErrorCode: Option[Int],
      mayBeThrowable: Option[Throwable]
  ) {
    def nel(): NonEmptyList[ServiceError] = NonEmptyList.one(this)
  }

  def fatalError(responseMsg: String, throwable: Throwable): ServiceError = {
    ServiceError(responseMsg, None, throwable.some)
  }

  def businessError(responseMsg: String, errorCode: Int): ServiceError = {
    ServiceError(responseMsg, errorCode.some, None)
  }

  object ServiceCodes {
    val INTERNAL_ERROR_MSG = "internal error"
    val NOT_FOUND = 404
    val BAD_REQUEST = 400
  }

  def validateForBlankString(input: String, serviceError: ServiceError): Either[NonEmptyList[ServiceError], String] = {
    import cats.implicits._
    val notCorrect = Option(input).map(_.trim).getOrElse("").isEmpty
    notCorrect match {
      case true  => serviceError.invalidNel[String].toEither
      case false => input.trim.validNel[ServiceError].toEither
    }
  }

  implicit class EitherThrowableExtension[T](either: Either[Throwable, T]) {
    def sEither(errorMsg: String): SEither[T] = {
      either.left.map(t => fatalError(errorMsg, t).nel())
    }
  }

  implicit class TryExtension[T](tried: Try[T]) {
    def sEither(errorMsg: String): SEither[T] = {
      tried.toEither.left.map(t => fatalError(errorMsg, t).nel())
    }
  }

  implicit class OptionExtension[T](option: Option[T]) {
    def sEither(serviceError: ServiceError): SEither[T] = {
      option.toRight( serviceError.nel() )
    }
  }

  type SEither[T] = Either[NonEmptyList[ServiceError], T]
}
