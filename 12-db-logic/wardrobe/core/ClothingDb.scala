package com.mindera
package wardrobe.core

import db._
import better.files.Resource

object ClothingDb {

  //fail fast
  val db: Db = {
    val str = Resource.getAsString("clothing_db.conf")
    val map = str.split("\n").filter(!_.isEmpty).map(_.split("=")).collect { case Array(key, v) => key -> v }.toMap

    val propertiesMap = PropertiesMap(map)
    val envMap = EnvMap(sys.env)

    val dbConfSEither = DbConfFactory.dbConfFromMap(propertiesMap, envMap, "POSTGRES_HOST", "POSTGRES_PASSWORD",
      "CLOTHING_DB_PORT", "CLOTHING_DATABASE", "CLOTHING_DB_USER")

    dbConfSEither match {
      case Left(serviceErrorsNel) =>
        val errorMsg = serviceErrorsNel.iterator.toList.mkString("\n")
        throw new RuntimeException(errorMsg)
      case Right(dbConf) => PostgresDb(dbConf)
    }


  }
}
