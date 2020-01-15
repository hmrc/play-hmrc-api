/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.api.controllers

abstract class ErrorResponse(
  val httpStatusCode: Int,
  val errorCode:      String,
  val message:        String)

case object ErrorUnauthorized extends ErrorResponse(401, "UNAUTHORIZED", "Bearer token is missing or not authorized")

case object ErrorUnauthorizedLowCL
    extends ErrorResponse(401, "LOW_CONFIDENCE_LEVEL", "Confidence Level on account does not allow access")

case object ErrorNotFound extends ErrorResponse(404, "NOT_FOUND", "Resource was not found")

case class ErrorGenericBadRequest(msg: String = "Bad Request") extends ErrorResponse(400, "BAD_REQUEST", msg)

object ErrorGenericBadRequest {

  import play.api.data.validation.ValidationError
  import play.api.libs.json.{JsError, JsPath}

  def apply(errors: Seq[(JsPath, Seq[ValidationError])]) =
    new ErrorGenericBadRequest(JsError.toFlatJson(errors).as[String])
}

case object ErrorAcceptHeaderInvalid
    extends ErrorResponse(406, "ACCEPT_HEADER_INVALID", "The accept header is missing or invalid")

case object ErrorInternalServerError extends ErrorResponse(500, "INTERNAL_SERVER_ERROR", "Internal server error")

case object PreferencesSettingsError
    extends ErrorResponse(500, "PREFERENCE_SETTINGS_ERROR", "Failed to set preferences")

object ErrorResponse {
  import play.api.libs.json.{JsValue, Json, Writes}

  implicit val writes = new Writes[ErrorResponse] {
    def writes(e: ErrorResponse): JsValue = Json.obj("code" -> e.errorCode, "message" -> e.message)
  }
}
