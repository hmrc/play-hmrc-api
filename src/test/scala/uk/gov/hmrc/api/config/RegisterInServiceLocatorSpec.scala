package uk.gov.hmrc.api.config

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.test.FakeApplication
import uk.gov.hmrc.api.connector.ServiceLocatorConnector
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future

class RegisterInServiceLocatorSpec extends UnitSpec with MockitoSugar {

  trait Setup extends ServiceLocatorRegistration with ServiceLocatorConfig {
    val mockConnector = mock[ServiceLocatorConnector]
    override val slConnector = mockConnector
    override implicit val hc: HeaderCarrier = HeaderCarrier()
  }

  "onStart" should {
    "register the microservice in service locator when registration is enabled" in new Setup {
      override lazy val registrationEnabled: Boolean = true

      when(mockConnector.register(any())).thenReturn(Future.successful(true))
      onStart(FakeApplication())
      verify(mockConnector).register(any())
    }

    "not register the microservice in service locator when registration is disabled" in new Setup {
      override lazy val registrationEnabled: Boolean = false
      onStart(FakeApplication())
      verify(mockConnector, never()).register(any())
    }
  }
}
