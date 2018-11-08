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

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.BeforeAndAfterAll
import play.api.mvc.{AnyContent, BodyParser, DefaultPlayBodyParsers, PlayBodyParsers}
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext

class HeaderValidatorSpec extends UnitSpec with HeaderValidator with BeforeAndAfterAll {

  "acceptHeaderValidationRules" should {
    "return false when the header value is missing" in {
      acceptHeaderValidationRules(None) shouldBe false
    }
  }

  "acceptHeaderValidationRules" should {
    "return true when the version and the content type in header value is well formatted" in {
      acceptHeaderValidationRules(Some("application/vnd.hmrc.1.0+json")) shouldBe true
    }
  }

  "acceptHeaderValidationRules" should {
    "return false when the content type in header value is missing" in {
      acceptHeaderValidationRules(Some("application/vnd.hmrc.1.0")) shouldBe false
    }
  }

  "acceptHeaderValidationRules" should {
    "return false when the content type in header value is not well formatted" in {
      acceptHeaderValidationRules(Some("application/vnd.hmrc.v1+json")) shouldBe false
    }
  }

  "acceptHeaderValidationRules" should {
    "return false when the content type in header value is not valid" in {
      acceptHeaderValidationRules(Some("application/vnd.hmrc.notvalid+XML")) shouldBe false
    }
  }

  "acceptHeaderValidationRules" should {
    "return false when the version in header value is not valid" in {
      acceptHeaderValidationRules(Some("application/vnd.hmrc.notvalid+json")) shouldBe false
    }
  }

  private implicit val actorSystem: ActorSystem = ActorSystem(getClass.getSimpleName)
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  override protected def afterAll(): Unit = {
    super.afterAll()
    await(actorSystem.terminate())
  }

  override protected val parser: BodyParser[AnyContent] = PlayBodyParsers().default
  override protected val executionContext: ExecutionContext = ExecutionContext.global
}
