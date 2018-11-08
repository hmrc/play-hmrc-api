/*
 * Copyright 2018 HM Revenue & Customs
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

import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex
import scala.util.matching.Regex.Match


trait HeaderValidator extends Results {

  val validateVersion: String => Boolean = _ == "1.0"

  val validateContentType: String => Boolean = _ == "json"

  val matchHeader: String => Option[Match] = new Regex( """^application/vnd[.]{1}hmrc[.]{1}(.*?)[+]{1}(.*)$""", "version", "contenttype") findFirstMatchIn _

  val acceptHeaderValidationRules: Option[String] => Boolean =
    _ flatMap (a => matchHeader(a) map (res => validateContentType(res.group("contenttype")) && validateVersion(res.group("version")))) getOrElse (false)

  // Classes can provide these by having  cc: ControllerComponents injected
//  override protected val parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser
//  override protected val executionContext: ExecutionContext = cc.executionContext
  protected val parser: BodyParser[AnyContent]
  protected val executionContext: ExecutionContext



  def validateAccept(rules: Option[String] => Boolean): ActionBuilder[Request, AnyContent] = new ActionBuilder[Request, AnyContent] {
    override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
      if (rules(request.headers.get("Accept"))) block(request)
      else Future.successful(Status(ErrorAcceptHeaderInvalid.httpStatusCode)(Json.toJson(ErrorAcceptHeaderInvalid)))
    }

    override def parser: BodyParser[AnyContent] = HeaderValidator.this.parser

    override protected def executionContext: ExecutionContext = HeaderValidator.this.executionContext
  }
}
