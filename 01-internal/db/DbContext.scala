package com.mindera
package db

import cats.effect.{Blocker, ContextShift, IO}
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

trait DbContext {
  implicit val cs: ContextShift[IO] =
    IO.contextShift(ExecutionContexts.synchronous)
  val blocker: Blocker =
    Blocker.liftExecutionContext(ExecutionContexts.synchronous)

  implicit def xa(db: Db): Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    db.dbDriver,
    db.dbUrl,
    db.dbUser,
    db.dbPassword,
    blocker
  )
}
