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

package uk.gov.hmrc.api.config

import play.api.{Application, GlobalSettings, Logger}
import uk.gov.hmrc.api.connector.ServiceLocatorConnector
import uk.gov.hmrc.play.config.RunMode
import uk.gov.hmrc.play.http.HeaderCarrier

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
}
