/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.api.connector

import play.api.Logger
import uk.gov.hmrc.api.config.ServiceLocatorConfig
import uk.gov.hmrc.api.domain.Registration
import uk.gov.hmrc.play.config.AppName
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ServiceLocatorConnector {
  val appName: String
  val appUrl: String
  val serviceUrl: String
  val handlerOK: () => Unit
  val handlerError: Throwable => Unit
  val metadata: Option[Map[String, String]]
  val http: HttpPost

  def register(implicit hc: HeaderCarrier): Future[Boolean] = {
    val registration = Registration(appName, appUrl, metadata)
    http.POST(s"$serviceUrl/registration", registration, Seq("Content-Type" -> "application/json")) map {
      _ =>
        handlerOK()
        true
    } recover {
      case e: Throwable =>
        handlerError(e)
        false
    }
  }
}


sealed trait ServiceLocatorConnectorConfig extends ServiceLocatorConnector with ServiceLocatorConfig with AppName {
  import play.api.Play.current

  lazy val appUrl = current.configuration.getString("appUrl").getOrElse(throw new RuntimeException("appUrl is not configured"))
  lazy val serviceUrl = serviceLocatorUrl

  val handlerOK: () => Unit = () => Logger.info("Service is registered on the service locator")
  val handlerError: Throwable => Unit = e => Logger.error(s"Service could not register on the service locator", e)
  val metadata: Option[Map[String, String]] = Some(Map("third-party-api" -> "true"))
}

object ServiceLocatorConnector {
  def apply(httpPost: HttpPost): ServiceLocatorConnector = new ServiceLocatorConnectorConfig{
    override val http: HttpPost = httpPost
  }
}
