package com.mindera
package wardrobe.http.get

import common.CommonErrorOps
import json.CaseClassJsonizer
import wardrobe.adts.Clothing
import wardrobe.core.ClothingDbPersistenceService
import wardrobe.http.handler.ResponseEntityHandler

import cats.data.NonEmptyList
import com.mindera.wardrobe.adts.ClothingPersistenceService._
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, PathVariable}

@Controller
class ListClothingByNameController extends CaseClassJsonizer with ResponseEntityHandler with CommonErrorOps {



  @GetMapping(value = Array("/v1/clothes/name/{ss}"))
  def clothesByName(@PathVariable("ss") ss: String): ResponseEntity[String] = {

    val kliesli = for{
      clothingNel <- fetchClothes(ss)
      json <- rLift( prettyJson(clothingNel.toList).sEither(ServiceCodes.INTERNAL_ERROR_MSG) )
    } yield json


    val eitherPayload = kliesli.run(new ClothingDbPersistenceService{})


    payloadResponseHandler(eitherPayload, "/v1/clothes/", ss)
  }

  def fetchClothes(searchString: String): ClothingOps[NonEmptyList[Clothing]] = {

    for {
      sanitizedString <- rLift( validateForBlankString(searchString, searchStringError(searchString)) )
      clothingSeq <- fetchClothing(sanitizedString) //Right(Seq()) //fetch(sanitizedString)
      mayBeClothingChain <- rLift( NonEmptyList.fromList(clothingSeq.toList).sEither( noClothesFoundError(searchString) ) )
    } yield mayBeClothingChain
  }
}