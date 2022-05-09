package com.mindera
package wardrobe.core

import wardrobe.adts.Clothing

import cats.data.NonEmptyList

object ClothingOps {

  def searchStringError(input: String): ServiceError = {
    businessError(s"search string >$input< can't be empty", ServiceCodes.BAD_REQUEST)
  }

  def noClothesFoundError(searchString: String): ServiceError = {
    businessError(s"no clothing found for search string >$searchString<", ServiceCodes.BAD_REQUEST)
  }

  def fetchClothesByName(searchString: String, fetch: String => SEither[Seq[Clothing]]): SEither[NonEmptyList[Clothing]] = {

    for {
      sanitizedString <- validateForBlankString(searchString, searchStringError(searchString))
      clothingSeq <- fetch(sanitizedString)
      mayBeClothingChain <- NonEmptyList.fromList(clothingSeq.toList).sEither( noClothesFoundError(searchString) )
    } yield mayBeClothingChain
  }

  def addClothing(clothing: Clothing, save: Clothing => SEither[Clothing]): SEither[Clothing] = {

    for {
      sanitizedClothing <- clothing.validate()
      persistedClothing <- save(sanitizedClothing)
    } yield persistedClothing
  }
}
