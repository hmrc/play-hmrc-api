/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.api.config

import play.api.mvc.{Handler, RequestHeader}
import play.api.{Application, GlobalSettings, Logger}
import uk.gov.hmrc.api.connector.ServiceLocatorConnector
import uk.gov.hmrc.play.config.RunMode

import scala.util.matching.Regex
import uk.gov.hmrc.http.HeaderCarrier

trait ServiceLocatorRegistration extends GlobalSettings with RunMode {
  self : ServiceLocatorConfig =>

  val registrationEnabled: Boolean
  val slConnector: ServiceLocatorConnector
  implicit val hc: HeaderCarrier

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    registrationEnabled match {
      case true => slConnector.register
      case false => Logger.warn("Registration in Service Locator is disabled")
    }
  }

  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    val overrideRequest = router.fold(request) {
      router => request.headers.get(router._1) match {
        case Some(value) =>
          val found = new Regex(router._2) findFirstIn value
          found.fold(request) { _ => Logger.info(s"Overriding request due to $router");request.copy(path = router._3 + request.path) }
        case _ => request
      }
    }
    super.onRouteRequest(overrideRequest)
  }

}
