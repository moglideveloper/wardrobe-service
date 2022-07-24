package com.mindera
package wardrobe.adts

import cats.data.ReaderT

trait ClothingPersistenceService {

  def fetch(ss : String) : SEither[Seq[Clothing]]
}

object ClothingPersistenceService{


  type ClothingOps[A] = ReaderT[SEither, ClothingPersistenceService, A]

  def fetchClothing(ss : String) : ClothingOps[Seq[Clothing]] = ReaderT[SEither, ClothingPersistenceService, Seq[Clothing]] { service : ClothingPersistenceService =>
      service.fetch(ss)
  }

  def rLift[A](either : SEither[A]) : ClothingOps[A] = {
    ReaderT.liftF[SEither, ClothingPersistenceService, A](either)
  }
}
