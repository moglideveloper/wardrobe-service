package com.mindera
package wardrobe.http.post

import json.CaseClassJsonizer
import wardrobe.adts.Clothing
import wardrobe.core.{ClothingDb, ClothingOps, DbClothingOps}
import wardrobe.http.handler.ResponseEntityHandler

import org.springframework.http.{HttpEntity, ResponseEntity}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, PostMapping}

@Controller
class AddClothingController extends CaseClassJsonizer with ResponseEntityHandler {

  val wrongInputError = "input malformed"
  def addClothingFunction: Clothing => SEither[Clothing] = (DbClothingOps.addClothing _).curried(ClothingDb.db)

  @PostMapping(value = Array("/v1/add"))
  def addClothes(httpEntity: HttpEntity[String]): ResponseEntity[String] = {

    val json = httpEntity.getBody

    val eitherPayload: SEither[String] = for {
      clothing <- fromJson[Clothing](json).sEither(wrongInputError)
      persistedClothing <- ClothingOps.addClothing(clothing, addClothingFunction)
      json <- prettyJson(persistedClothing).sEither(ServiceCodes.INTERNAL_ERROR_MSG)
    } yield json

    payloadResponseHandler(eitherPayload, "/v1/add/", json)
  }
}