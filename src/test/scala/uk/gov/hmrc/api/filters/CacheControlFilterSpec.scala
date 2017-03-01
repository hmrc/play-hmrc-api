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

package uk.gov.hmrc.api.filters

import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpecLike}
import play.api.http.HeaderNames
import play.api.http.HttpVerbs.{GET,POST}
import play.api.mvc.{Result, _}
import play.api.test.FakeRequest
import play.api.test.{FakeApplication, _}
import uk.gov.hmrc.play.test.WithFakeApplication
import scala.concurrent.Future

class CacheControlFilterSpec extends WordSpecLike with Matchers with MockitoSugar with ScalaFutures {

  private trait Setup extends Results {

    val resultFromAction: Result = Ok

    val expectedEndPoint = "/foo/bar$" -> 1234
    val expectedCacheControlHeader = HeaderNames.CACHE_CONTROL -> ("max-age=" + expectedEndPoint._2)
    val expectedEndPointRegex = "/foo/\\d+/\\w" -> 4567
    val expectedCacheControlHeaderRegex = HeaderNames.CACHE_CONTROL -> ("max-age=" + expectedEndPointRegex._2)
    val expectedNoCacheControlHeader = HeaderNames.CACHE_CONTROL -> "no-cache,no-store,max-age=0"

    val cacheControlFilter = new CacheControlFilter {
      val cachedEndPoints = Map(expectedEndPoint, expectedEndPointRegex)
    }

    lazy val action = {
      val mockAction = mock[(RequestHeader) => Future[Result]]
      val outgoingResponse = Future.successful(resultFromAction)
      when(mockAction.apply(any())).thenReturn(outgoingResponse)
      mockAction
    }

    def requestPassedToAction = {
      val updatedRequest = ArgumentCaptor.forClass(classOf[RequestHeader])
      verify(action).apply(updatedRequest.capture())
      updatedRequest.getValue
    }
  }

  "During request pre-processing, the filter" should {

    "do nothing, just pass on the request" in new Setup {
      cacheControlFilter(action)(FakeRequest())
      requestPassedToAction should ===(FakeRequest())
    }
  }

  "During result post-processing, the filter" should {

    "add a cache-control header with the correct max-age value given a url defined in application config" in new Setup {
      cacheControlFilter(action)(FakeRequest("GET", "/foo/bar")).futureValue should be(resultFromAction.withHeaders(expectedCacheControlHeader))
    }

    "add a cache-control header with the correct max-age value given a url defined using a regex in application config" in new Setup {
      cacheControlFilter(action)(FakeRequest("GET", "/foo/1234/abc" )).futureValue should be(resultFromAction.withHeaders(expectedCacheControlHeaderRegex))
    }

    "ignore query string parameters" in new Setup {
      cacheControlFilter(action)(FakeRequest("GET", "/foo/bar?foo=bar&baz=1234")).futureValue should be(resultFromAction.withHeaders(expectedCacheControlHeader))
    }

    "add a cache-control header with no-cache for urls not defined in application config" in new Setup {
      cacheControlFilter(action)(FakeRequest(GET, "/foo/bar/baz")).futureValue should be(resultFromAction.withHeaders(expectedNoCacheControlHeader))
    }

    "add a cache-control header with no-cache given an http method other than GET" in new Setup {
      cacheControlFilter(action)(FakeRequest(POST, expectedEndPoint._1)).futureValue should be(resultFromAction.withHeaders(expectedNoCacheControlHeader))
    }
  }

  "Creating the filter from config" should {
    "throw an exception on missing config" in  {
      an [RuntimeException] should be thrownBy CacheControlFilter.fromConfig(CacheControlFilter.configKey).cachedEndPoints
    }
  }

}

class CacheControlFilterWithAppSpec extends WordSpecLike with Matchers with MockitoSugar with ScalaFutures with WithFakeApplication {

  val additionalConfiguration: Map[String, Any] = Map("apiCaching" -> Map("/zark/snork" -> 1234, "/splish/splash" -> 5678))
  override lazy val fakeApplication = FakeApplication(additionalConfiguration = additionalConfiguration)

  "Creating the filter from config" should {

    "load the correct values" in  {
        CacheControlFilter.fromConfig(CacheControlFilter.configKey).cachedEndPoints should be(Map("/zark/snork" -> 1234, "/splish/splash" -> 5678))
      }
    }
}
