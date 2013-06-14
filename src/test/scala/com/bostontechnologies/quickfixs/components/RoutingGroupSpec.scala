package com.bostontechnologies.quickfixs.components

import quickfix.field.RoutingType
import org.specs.SpecificationWithJUnit

class RoutingGroupSpec extends SpecificationWithJUnit {

  "A RoutingGroup" should {
    "convert to and from fix" in {
      val expected = RoutingGroup("routing Id!!!", RoutingType.TARGET_FIRM)
      val actual = RoutingGroup(expected.toFields)

      actual.groupId must be equalTo (expected.groupId)
      actual.routingType must be equalTo (expected.routingType)
    }

    "Default to RoutingType.TARGET_LIST" in {
      val expected = RoutingGroup("routing Id!!!")
      val actual = RoutingGroup(expected.toFields)

      actual.groupId must be equalTo (expected.groupId)
      actual.routingType must be equalTo (expected.routingType)
    }

  }
}