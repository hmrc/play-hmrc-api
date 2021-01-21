/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.service

import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector, AuditResult}
import uk.gov.hmrc.play.audit.model.DataEvent

import scala.concurrent.{ExecutionContext, Future}

trait Auditor {

  lazy val auditType: String = "ServiceResponseSent"
  val auditConnector: AuditConnector

  /**
    * You can get the appName by injecting it into your constructor using:
    *
    * `@Named("appName") override val appName: String`
    */
  def appName: String

  def withAudit[T](
    service:     String,
    details:     Map[String, String]
  )(func:        Future[T]
  )(implicit hc: HeaderCarrier,
    ec:          ExecutionContext
  ): Future[T] = {
    audit(service, details) // No need to wait!
    func
  }

  protected def audit(
    service:     String,
    details:     Map[String, String]
  )(implicit hc: HeaderCarrier,
    ec:          ExecutionContext
  ): Future[AuditResult] =
    auditConnector.sendEvent(DataEvent(appName, auditType, tags = Map("transactionName" -> service), detail = details))
}
