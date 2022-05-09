package com.mindera
package wardrobe.adts

import cats.implicits._
import wardrobe.adts.Clothing._

case class Clothing(name : String, category : String){

  def validate() : SEither[Clothing] = {
    val nameSEither = validateForBlankString(name, clothingNameError(name))
    val categorySEither = validateForBlankString(category, clothingCategoryError(category))
    val result = (nameSEither, categorySEither).mapN( Clothing.apply )
    result
  }
}

object Clothing{
  def clothingNameError(input: String): ServiceError = {
    businessError(s"clothing name >$input< can't be empty", ServiceCodes.BAD_REQUEST)
  }

  def clothingCategoryError(input: String): ServiceError = {
    businessError(s"clothing category >$input< can't be empty", ServiceCodes.BAD_REQUEST)
  }
}