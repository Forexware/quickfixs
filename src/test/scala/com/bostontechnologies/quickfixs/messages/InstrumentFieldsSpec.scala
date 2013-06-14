package com.bostontechnologies.quickfixs.messages

import quickfix.{Message, FieldNotFound}
import quickfix.field.{SecurityType, Product, Symbol}
import com.bostontechnologies.quickfixs.components.Instrument
import org.specs.SpecificationWithJUnit

class InstrumentFieldsSpec extends SpecificationWithJUnit {

  private val fields = new RichMessage(new Message) with InstrumentFields[Message]
  private val symbol = "EURUSD"
  private val product = 1
  private val securityType = "FOR"

  "An InstrumentFields" should {

    "provide a way to manage symbol field" in {
      fields.hasSymbol must beFalse
      fields.self.isSetField(Symbol.FIELD) must beFalse
      fields.symbol must throwA[FieldNotFound]

      fields.self.setString(Symbol.FIELD, symbol)
      fields.hasSymbol must beTrue
      fields.symbol must be equalTo (symbol)

      fields.self.clear()

      fields.symbol = symbol
      fields.hasSymbol must beTrue
      fields.symbol must be equalTo (symbol)
      fields.self.getString(Symbol.FIELD) must be equalTo (symbol)
    }

    "provide a way to manage product field" in {
      fields.hasProduct must beFalse
      fields.self.isSetField(Product.FIELD) must beFalse
      fields.product must throwA[FieldNotFound]

      fields.self.setInt(Product.FIELD, product)
      fields.hasProduct must beTrue
      fields.product must be equalTo (product)

      fields.self.clear()

      fields.product = product
      fields.hasProduct must beTrue
      fields.product must be equalTo (product)
      fields.self.getInt(Product.FIELD) must be equalTo (product)
    }

    "provide a way to manage securityType field" in {
      fields.hasSecurityType must beFalse
      fields.self.isSetField(SecurityType.FIELD) must beFalse
      fields.securityType must throwA[FieldNotFound]

      fields.self.setString(SecurityType.FIELD, securityType)
      fields.hasSecurityType must beTrue
      fields.securityType must be equalTo (securityType)

      fields.self.clear()

      fields.securityType = securityType
      fields.hasSecurityType must beTrue
      fields.securityType must be equalTo (securityType)
      fields.self.getString(SecurityType.FIELD) must be equalTo (securityType)
    }

    "provide a way to manage instrument component" in {
      fields.self.setString(Symbol.FIELD, symbol)
      fields.instrument.symbol.get must be equalTo (symbol)

      fields.self.clear()

      fields.instrument = Instrument(symbol)
      fields.instrument.symbol.get must be equalTo (symbol)
      fields.self.getString(Symbol.FIELD) must be equalTo (symbol)
    }

  }

}
