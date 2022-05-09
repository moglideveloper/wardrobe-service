package com.mindera
package db

import scala.util.Try

object DbConfFactory {

  def dbConfFromMap(
                     propsMap: PropertiesMap, envMap: EnvMap,
                     hostKey: String, passwordKey: String,
                     portKey: String, databaseKey: String, userKey: String
                   ): SEither[DbConf] = {

    for {
      hostName <- (envMap(hostKey) orElse propsMap(hostKey)).sEither(businessError(s"$hostKey not found", ServiceCodes.NOT_FOUND))
      password <- (envMap(passwordKey) orElse propsMap(passwordKey)).sEither(businessError(s"$passwordKey not found", ServiceCodes.NOT_FOUND))
      portString <- (envMap(portKey) orElse propsMap(portKey)).sEither(businessError(s"$portKey not found", ServiceCodes.NOT_FOUND))
      portInt <- Try(portString.toInt).sEither(s"$portKey is >$portString< and it should be integer")
      database <- (envMap(databaseKey) orElse propsMap(databaseKey)).sEither(businessError(s"$databaseKey not found", ServiceCodes.NOT_FOUND))
      user <- (envMap(userKey) orElse propsMap(userKey)).sEither(businessError(s"$userKey not found", ServiceCodes.NOT_FOUND))
    } yield DbConf(hostName, portInt, database, user, password)
  }
}
