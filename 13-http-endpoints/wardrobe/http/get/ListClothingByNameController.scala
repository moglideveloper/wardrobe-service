package com.mindera
package wardrobe.http.get

import json.CaseClassJsonizer
import wardrobe.adts.Clothing
import wardrobe.core.{ClothingDb, ClothingOps, DbClothingOps}
import wardrobe.http.handler.ResponseEntityHandler

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, PathVariable}

@Controller
class ListClothingByNameController extends CaseClassJsonizer with ResponseEntityHandler{

  def searchByNameFunction: String => SEither[Seq[Clothing]] = (DbClothingOps.fetchClothingByName _).curried(ClothingDb.db)

  @GetMapping(value = Array("/v1/clothes/name/{ss}"))
  def clothesByName(@PathVariable("ss") ss: String): ResponseEntity[String] = {

    val eitherPayload: SEither[String] = for{
      clothingNel <- ClothingOps.fetchClothesByName(ss, searchByNameFunction)
      json <- prettyJson(clothingNel.toList).sEither(ServiceCodes.INTERNAL_ERROR_MSG)
    } yield json

    payloadResponseHandler(eitherPayload, "/v1/clothes/", ss)
  }
}