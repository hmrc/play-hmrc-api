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

package uk.gov.hmrc.api.connector

import com.google.inject.{ImplementedBy, Singleton}
import javax.inject.Inject
import play.api.Mode.Mode
import play.api.http.ContentTypes.JSON
import play.api.http.HeaderNames.CONTENT_TYPE
import play.api.{Configuration, Environment, Logger}
import uk.gov.hmrc.api.config.ServiceLocatorConfig
import uk.gov.hmrc.api.domain.Registration
import uk.gov.hmrc.http.{CorePost, HeaderCarrier}
import uk.gov.hmrc.play.bootstrap.config.AppName

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[ApiServiceLocatorConnector])
trait ServiceLocatorConnector {
  def appName: String
  val appUrl: String
  val serviceUrl: String
  val handlerOK: () => Unit
  val handlerError: Throwable => Unit
  val metadata: Option[Map[String, String]]
  val http: CorePost

  def register(implicit hc: HeaderCarrier): Future[Boolean] = {
    val registration = Registration(appName, appUrl, metadata)
    http.POST(s"$serviceUrl/registration", registration, Seq(CONTENT_TYPE -> JSON)) map {
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

@Singleton
class ApiServiceLocatorConnector @Inject()(override val runModeConfiguration: Configuration, environment: Environment, override val http: CorePost)
  extends ServiceLocatorConnector with ServiceLocatorConfig with AppName {
  override val appUrl: String = runModeConfiguration.getString("appUrl").getOrElse(throw new RuntimeException("appUrl is not configured"))
  override val serviceUrl: String = serviceLocatorUrl
  override val handlerOK: () => Unit = () => Logger.info("Service is registered on the service locator")
  override val handlerError: Throwable => Unit = e => Logger.error("Service could not register on the service locator", e)
  override val metadata: Option[Map[String, String]] = Some(Map("third-party-api" -> "true"))
  override def configuration: Configuration = runModeConfiguration
  override protected def mode: Mode = environment.mode
}
