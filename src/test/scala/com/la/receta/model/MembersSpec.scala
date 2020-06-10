package com.la.receta.model

import com.la.receta.database.{Connector, SchemaMigration}
import com.la.receta.entities.CreateMemberRequest
import com.la.receta.testkit.RecetaTestkit
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MembersSpec extends AnyWordSpec with Matchers with ScalaFutures with IntegrationPatience with RecetaTestkit {

  "Members" should {
    "create member with valid request" in {

      val request = CreateMemberRequest(s"$string10", s"$string10@example.com", "password")

      val result = memberDb.insert(request).futureValue
      val member = memberDb.findById(result).futureValue

      member.isDefined shouldBe true
      member.get.userId shouldEqual result
      member.get.username shouldEqual request.username
      member.get.email shouldEqual request.email
    }
  }
}