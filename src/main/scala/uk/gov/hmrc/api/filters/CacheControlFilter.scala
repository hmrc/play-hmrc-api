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

package uk.gov.hmrc.api.filters

import akka.stream.Materializer
import play.api.Play
import play.api.Play.current
import play.api.http.HttpVerbs.{GET => GET_METHOD}
import play.api.mvc.{Filter, RequestHeader, Result}
import play.mvc.Http.HeaderNames

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex

abstract class CacheControlFilter extends Filter {
  implicit def mat: Materializer = Play.materializer

  val cachedEndPoints: Map[String, Int]

  final def apply(f: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    f(requestHeader).map(result => {
      val maybeMaxAge = cachedEndPoints.find{ p => new Regex(p._1).findAllIn(requestHeader.path).nonEmpty }.map{ p => p._2 }
      (requestHeader.method, maybeMaxAge) match {
        case (GET_METHOD, Some(maxAge)) => result.withHeaders(HeaderNames.CACHE_CONTROL -> ("max-age=" + maxAge))
        case _ => result.withHeaders(HeaderNames.CACHE_CONTROL -> "no-cache,no-store,max-age=0")
      }
    })
  }
}

object CacheControlFilter {
  val configKey = "apiCaching"

  def fromConfig(configKey: String) = {
    new CacheControlFilter {
      override lazy val cachedEndPoints = {
        Play.current.configuration.getObject(configKey).map(_.asScala).iterator.flatMap(a => a.toMap).map{ a => a._1 -> Int.unbox(a._2.unwrapped()) }.toMap
      }
    }
  }
}
