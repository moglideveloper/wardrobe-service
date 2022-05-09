package com.mindera
package db

trait Db{
  val hostname: String
  val port: Int
  val schema: String
  val dbUrl: String
  val dbUser: String
  val dbPassword: String
  val dbDriver: String
}

case class DbConf(hostname : String, port : Int, schema : String, dbUser : String, dbPassword : String)

case class PostgresDb(dbConf: DbConf) extends Db{
  override val hostname: String = dbConf.hostname
  override val port: Int = dbConf.port
  override val schema: String = dbConf.schema
  override val dbUrl: String = s"jdbc:postgresql://$hostname:$port/$schema"
  override val dbUser: String = dbConf.dbUser
  override val dbPassword: String = dbConf.dbPassword
  override val dbDriver: String = "org.postgresql.Driver"
}

