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

import play.api.Play._
import uk.gov.hmrc.play.config.ServicesConfig

trait ServiceLocatorConfig extends ServicesConfig {
  lazy val serviceLocatorUrl: String = baseUrl("service-locator")
  lazy val registrationEnabled: Boolean = current.configuration.getBoolean(s"microservice.services.service-locator.enabled").getOrElse(true)

  lazy val header = current.configuration.getString(s"router.header")
  lazy val regex = current.configuration.getString(s"router.regex")
  lazy val prefix = current.configuration.getString(s"router.prefix")

  lazy val router = {
    (header, regex, prefix) match {
      case (Some(a:String), Some(b:String), Some(c:String)) => Some(a, b, c)
      case _ => None
    }
  }

}
