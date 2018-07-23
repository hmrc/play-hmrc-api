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

package uk.gov.hmrc.api.sandbox

import javax.inject.{Inject, Singleton}
import play.api.http.{HttpConfiguration, HttpErrorHandler, HttpFilters}
import play.api.mvc.{Handler, RequestHeader}
import play.api.routing.Router
import play.api.{Configuration, Environment, Logger}
import uk.gov.hmrc.play.bootstrap.http.RequestHandler

import scala.util.matching.Regex

@Singleton
class RoutingHttpRequestHandler @Inject()(router: Router, errorHandler: HttpErrorHandler, configuration: HttpConfiguration,
                                          filters: HttpFilters, environment: Environment, runConfiguration: Configuration)
  extends RequestHandler(router: Router, errorHandler: HttpErrorHandler, configuration: HttpConfiguration, filters: HttpFilters) {

  lazy val header = runConfiguration.getString(s"router.header")
  lazy val regex = runConfiguration.getString(s"router.regex")
  lazy val prefix = runConfiguration.getString(s"router.prefix")

  lazy val routing: Option[(String, String, String)] = {
    (header, regex, prefix) match {
      case (Some(a: String), Some(b: String), Some(c: String)) => Some((a, b, c))
      case _ => None
    }
  }

  override def routeRequest(request: RequestHeader): Option[Handler] = {
    super.routeRequest(overrideRouting(request))
  }

  def overrideRouting(request: RequestHeader): RequestHeader = {
    routing.fold(request) { routing =>
      request.headers.get(routing._1) match {
        case Some(value) =>
          val found = new Regex(routing._2) findFirstIn value
          found.fold(request) {
            _ => {
              Logger.info(s"Overriding request due to $routing")
              request.copy(path = routing._3 + request.path)
            }
          }
        case _ => request
      }
    }
  }
}
