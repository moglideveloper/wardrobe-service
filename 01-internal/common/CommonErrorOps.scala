package com.mindera
package common

trait CommonErrorOps {

  def searchStringError(input: String): ServiceError = {
    businessError(
      s"search string >$input< can't be empty",
      ServiceCodes.BAD_REQUEST
    )
  }

  def noClothesFoundError(searchString: String): ServiceError = {
    businessError(
      s"no clothing found for search string >$searchString<",
      ServiceCodes.BAD_REQUEST
    )
  }
}
