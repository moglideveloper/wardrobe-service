package com.mindera
package wardrobe.main

import json.CaseClassJsonizer
import wardrobe.adts.Clothing

import better.files.Dsl._
import cats.data._
import cats.implicits._

object ValidateExample extends CaseClassJsonizer{

  val searchStringError = businessError(s"search string >$ss< can't be empty", ServiceCodes.BAD_REQUEST)
  val ss = ""


  def main(args: Array[String]): Unit = {
    //example2()
    val file = cwd / "13-http-endpoints-resources" / "clothing.csv"
    val lines = file.lines.toSeq
    lines foreach {line =>
      val arr = line.split(",").map(_.trim)
      val name = arr(0)
      val category = arr(1)
      val sql = s"INSERT INTO CLOTHING (NAME, CATEGORY) VALUES ('$name', '$category');"
      println(sql)
    }
  }

  private def example1(): Unit = {
    val clothing = Clothing("Blue Cap", "Casual")
    val r = prettyJson(clothing)
    println(r)
  }


  private def example2(): Unit = {
    val v1: Either[NonEmptyList[ServiceError], String] = validateForBlankString(ss, searchStringError)
    val v2: Either[NonEmptyList[ServiceError], String] = validateForBlankString(ss, searchStringError)

    val list: Seq[Either[NonEmptyList[ServiceError], String]] = List(v1, v2)

    val t: Either[NonEmptyList[ServiceError], Seq[String]] = list.sequence

    println(t)
  }
}
