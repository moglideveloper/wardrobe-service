package com.mindera
package wardrobe.core

import db._
import wardrobe.adts.{Clothing, ClothingPersistenceService}

import doobie.implicits._

import scala.util.Try

trait ClothingDbPersistenceService extends DbContext with ClothingPersistenceService {

  val db = Infra.db

  def fetch(name: String): SEither[Seq[Clothing]] = {

    val query = {
      val selectFragment = sql"SELECT * FROM CLOTHING WHERE "
      val whereFragment = fr"NAME ILIKE ${"%" + name + "%"}"
      val sqlQuery = selectFragment ++ whereFragment
      println(s"$name : " + sqlQuery)
      sql"$sqlQuery".query[Clothing]
    }

    val tryQueryExecution = Try {
      val data = query.to[List].transact(xa(db)).unsafeRunSync()
      data
    }

    tryQueryExecution.sEither(ServiceCodes.INTERNAL_ERROR_MSG)
  }
}
