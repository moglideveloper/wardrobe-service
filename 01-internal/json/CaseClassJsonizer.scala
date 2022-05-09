package com.mindera
package json

import json.CaseClassJsonizer.{mapper, prettyJsonWriter}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{ClassTagExtensions, DefaultScalaModule, JavaTypeable}

import scala.util.Try

trait CaseClassJsonizer {

  def fromJson[T](json: String)(implicit m: JavaTypeable[T]): Either[Throwable, T] = {
    Try(mapper.readValue[T](json)).toEither
  }

  def toJsonString(ref: Any): Either[Throwable, String] = {
    Try(mapper.writeValueAsString(ref)).toEither
  }

  def jsonifyString(str : String): String = {
    mapper.writeValueAsString(str)
  }

  def jsonifyStrings(strings : Seq[String]): String = {
    mapper.writeValueAsString(strings)
  }

  def prettyJson(ref: Any): Either[Throwable, String] = {
    Try(prettyJsonWriter.writeValueAsString(ref)).toEither
  }
}

object CaseClassJsonizer{

  val mapper = mapperWithScalaModule()

  private def mapperWithScalaModule(): ObjectMapper with ClassTagExtensions = {
    val scalaObjectMapper = new ObjectMapper() with ClassTagExtensions
    scalaObjectMapper.registerModule(DefaultScalaModule)
    scalaObjectMapper
  }

  val prettyJsonWriter = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.writerWithDefaultPrettyPrinter
  }
}
