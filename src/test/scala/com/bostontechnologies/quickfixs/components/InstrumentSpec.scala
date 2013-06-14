package com.bostontechnologies.quickfixs.components

import quickfix.field.{Product, SecurityType, Symbol}
import quickfix.Message
import org.specs.SpecificationWithJUnit

class InstrumentSpec extends SpecificationWithJUnit {

  "An Instrument" should {

    "provide convenient set/get/clear methods" in {
      val instrument = Instrument("EURUSD")
      instrument.symbol.get must be equalTo ("EURUSD")
      instrument.product must be equalTo (None)
      instrument.product must be equalTo (None)
      instrument.securityType must be equalTo (None)

      instrument.product = 1
      instrument.securityType = "FOR"
      instrument.product.get must be equalTo (1)
      instrument.securityType.get must be equalTo ("FOR")

      instrument.clearProduct()
      instrument.product must be equalTo (None)
      instrument.clearSecurityType()
      instrument.securityType must be equalTo (None)
    }

    "be able to create a minimal FieldMap" in {
      val instrument = Instrument("EURUSD")
      val fields = instrument.toFields
      fields.getString(Symbol.FIELD) must be equalTo ("EURUSD")
      fields.isSetField(Product.FIELD) must beFalse
      fields.isSetField(SecurityType.FIELD) must beFalse
    }

    "be able to create a full FieldMap" in {
      val instrument = Instrument("EURUSD")
      instrument.product = 2
      instrument.securityType = "FOR"
      val fields = instrument.toFields
      fields.getString(Symbol.FIELD) must be equalTo ("EURUSD")
      fields.getInt(Product.FIELD) must be equalTo (2)
      fields.getString(SecurityType.FIELD) must be equalTo ("FOR")
    }

    "be created from a minimal FieldMap" in {
      val fields = new Message
      fields.setString(Symbol.FIELD, "EURUSD")
      val instrument = Instrument(fields)
      instrument.symbol.get must be equalTo ("EURUSD")
      instrument.product must be equalTo (None)
      instrument.securityType must be equalTo (None)
    }

    "be created from a full FieldMap" in {
      val fields = new Message
      fields.setString(Symbol.FIELD, "EURUSD")
      fields.setInt(Product.FIELD, 1)
      fields.setString(SecurityType.FIELD, "hello")
      val instrument = Instrument(fields)
      instrument.symbol.get must be equalTo ("EURUSD")
      instrument.product.get must be equalTo (1)
      instrument.securityType.get must be equalTo ("hello")
    }
  }

}
