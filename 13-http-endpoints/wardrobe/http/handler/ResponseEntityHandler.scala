package com.mindera
package wardrobe.http.handler

import json.CaseClassJsonizer

import org.slf4j.LoggerFactory
import org.springframework.http.{HttpStatus, ResponseEntity}

trait ResponseEntityHandler extends CaseClassJsonizer {

  val logger = LoggerFactory.getLogger(classOf[ResponseEntityHandler])

  def payloadResponseHandler(
      trySuccessJson: SEither[String],
      endpoint: String,
      args: String*
  ): ResponseEntity[String] = {

    trySuccessJson match {

      case Right(successJson) =>
        ResponseEntity.status(HttpStatus.OK).body(successJson)

      case Left(serviceErrorNel) =>
        val throwableSeq = serviceErrorNel collect {
          case ServiceError(responseMsg, _, Some(t)) => (responseMsg, t)
        }
        val errorResponses = serviceErrorNel.iterator.toList collect {
          case ServiceError(responseMsg, Some(x), _) => (responseMsg, x)
        }

        if (throwableSeq.length > 0) {
          val errorMsgDescription = s"error occurred while processing $endpoint with args >$args<"
          logger.error(errorMsgDescription)
          throwableSeq.foreach(t => logger.error(t._2.getMessage))
        }

        val allErrorResponses = throwableSeq.map(t => (t._1, HttpStatus.INTERNAL_SERVER_ERROR.value())) ++ errorResponses

        val errorMsgs = allErrorResponses.map(_._1)
        val errorJson = jsonifyStrings(errorMsgs)
        val effectiveHttpStatus = allErrorResponses.map(_._2).head
        ResponseEntity.status(HttpStatus.resolve(effectiveHttpStatus)).body(errorJson)
    }
  }
}
