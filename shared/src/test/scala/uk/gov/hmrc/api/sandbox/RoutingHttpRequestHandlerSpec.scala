/*
 * Copyright 2024 HM Revenue & Customs
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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.{ContentTypes, HeaderNames}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.typedmap.TypedMap
import play.api.mvc.request.{RemoteConnection, RequestTarget}
import play.api.mvc.{Headers, RequestHeader}

class RoutingHttpRequestHandlerSpec extends AnyWordSpec with Matchers with HeaderNames {

  class FakeRequestHeader(
    fakeHeaders: Headers,
    methodType:  String)
      extends RequestHeader {
    override val target:     RequestTarget    = RequestTarget("/context/some-path", "/context/some-path", Map.empty)
    override def method:     String           = methodType
    override def version:    String           = "HTTP/1.1"
    override def headers:    Headers          = fakeHeaders
    override def connection: RemoteConnection = RemoteConnection("", secure = true, None)
    override def attrs:      TypedMap         = TypedMap()
  }

  "RouterHttpRequestHandler" should {
    "route to standard controller when no X-MOBILE-USER-ID header is supplied" in {

      val application = new GuiceApplicationBuilder()
        .configure("router.regex" -> "208606423740")
        .configure("router.prefix" -> "/sandbox")
        .configure("router.header" -> "X-MOBILE-USER-ID")
        .build()

      val routingHttpRequestHandler = application.injector.instanceOf[RoutingHttpRequestHandler]

      val requestHeader = new FakeRequestHeader(
        new Headers(
          Seq((HOST, "localhost:8236"), (ACCEPT, "application/vnd.hmrc.1.0+json"), (CONTENT_TYPE, ContentTypes.JSON))
        ),
        "POST"
      )
      val overriddenRequest = routingHttpRequestHandler.overrideRouting(requestHeader)
      overriddenRequest.path shouldBe "/context/some-path"
      application.stop()
    }

    "route to standard controller when the X-MOBILE-USER-ID header doesn't match the configured regex" in {

      val application = new GuiceApplicationBuilder()
        .configure("router.regex" -> "100403398301")
        .configure("router.prefix" -> "/sandbox")
        .configure("router.header" -> "X-MOBILE-USER-ID")
        .build()

      val routingHttpRequestHandler = application.injector.instanceOf[RoutingHttpRequestHandler]

      val requestHeader = new FakeRequestHeader(
        new Headers(
          Seq((HOST, "localhost:8236"), (ACCEPT, "application/vnd.hmrc.1.0+json"), (CONTENT_TYPE, ContentTypes.JSON))
        ),
        "POST"
      )
      val overriddenRequest = routingHttpRequestHandler.overrideRouting(requestHeader)
      overriddenRequest.path shouldBe "/context/some-path"
      application.stop()
    }

    "route to standard controller when the X-MOBILE-USER-ID header partly matches the configured regex" in {
      val application = new GuiceApplicationBuilder()
        .configure("router.regex" -> "208606423740/929292929")
        .configure("router.prefix" -> "/sandbox")
        .configure("router.header" -> "X-MOBILE-USER-ID")
        .build()

      val routingHttpRequestHandler = application.injector.instanceOf[RoutingHttpRequestHandler]

      val requestHeader = new FakeRequestHeader(
        new Headers(
          Seq((HOST, "localhost:8236"), (ACCEPT, "application/vnd.hmrc.1.0+json"), (CONTENT_TYPE, ContentTypes.JSON))
        ),
        "POST"
      )
      val overriddenRequest = routingHttpRequestHandler.overrideRouting(requestHeader)
      overriddenRequest.path shouldBe "/context/some-path"
      application.stop()
    }

    "route to the sandbox controller when a valid X-MOBILE-USER-ID header is supplied" in {
      val application = new GuiceApplicationBuilder()
        .configure("router.regex" -> "208606423740")
        .configure("router.prefix" -> "/sandbox")
        .configure("router.header" -> "X-MOBILE-USER-ID")
        .build()

      val routingHttpRequestHandler = application.injector.instanceOf[RoutingHttpRequestHandler]

      val requestHeader = new FakeRequestHeader(new Headers(
                                                  Seq(("X-MOBILE-USER-ID", "208606423740"),
                                                      (HOST, "localhost:8236"),
                                                      (ACCEPT, "application/vnd.hmrc.1.0+json"),
                                                      (CONTENT_TYPE, ContentTypes.JSON))
                                                ),
                                                "POST")
      val overriddenRequest = routingHttpRequestHandler.overrideRouting(requestHeader)
      overriddenRequest.path shouldBe "/sandbox/context/some-path"
      application.stop()
    }
  }

}
