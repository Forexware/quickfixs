package com.bostontechnologies.quickfixs.components

import java.util.Date
import quickfix.field._
import java.math.BigDecimal
import com.bostontechnologies.quickfixs.Fix50
import org.specs.SpecificationWithJUnit
import Entry._

class EntrySpec extends SpecificationWithJUnit {

  private val myDate = new Date(1234567890)
  private val dateOnly = 1209600000
  private val timeOnly = 24967890

  "An Entry" should {

    "provide convenient set/get/clear methods" in {
      val entry: Entry = Entry(1, myDate)
      entry.entryType must be equalTo (1.toChar)
      entry.timestamp must be equalTo (myDate)
      entry.entryId must be equalTo (None)
      entry.size must be equalTo (None)
      entry.price must be equalTo (None)
      entry.quoteEntryId must be equalTo (None)
      entry.currency must be equalTo (None)

      entry.entryId = "bobSaget"
      entry.size = new BigDecimal(10)
      entry.price = new BigDecimal(1.23)
      entry.quoteEntryId = "jimmycarter"
      entry.currency = "USD"
      entry.entryId.get must be equalTo ("bobSaget")
      entry.size.get must be equalTo (new BigDecimal(10))
      entry.price.get must be equalTo (new BigDecimal(1.23))
      entry.quoteEntryId.get must be equalTo ("jimmycarter")
      entry.currency.get must be equalTo ("USD")


      entry.clearEntryId()
      entry.entryId must be equalTo (None)
      entry.clearSize()
      entry.size must be equalTo (None)
      entry.clearPrice()
      entry.price must be equalTo (None)
      entry.clearQuoteEntryId()
      entry.quoteEntryId must be equalTo (None)
      entry.clearCurrency()
      entry.currency must be equalTo (None)
    }

    "be able to create a minimal FieldMap" in {
      val entry: Entry = Entry('1')
      val fields = entry.toFields
      fields.getChar(MDEntryType.FIELD) must be equalTo (entry.entryType)
      fields.getUtcDateOnly(MDEntryDate.FIELD).getTime must notBeNull
      fields.getUtcTimeOnly(MDEntryTime.FIELD).getTime must notBeNull
      fields.isSetField(MDEntryPx.FIELD) must beFalse
      fields.isSetField(MDEntrySize.FIELD) must beFalse
      fields.isSetField(MDEntryID.FIELD) must beFalse
      fields.isSetField(QuoteEntryID.FIELD) must beFalse
    }

    "be able to create a FieldMap with a Date" in {
      val entry: Entry = Entry(1, myDate)
      val fields = entry.toFields
      fields.getChar(MDEntryType.FIELD) must be equalTo (entry.entryType)
      fields.getUtcDateOnly(MDEntryDate.FIELD).getTime must be equalTo (dateOnly)
      fields.getUtcTimeOnly(MDEntryTime.FIELD).getTime must be equalTo (timeOnly)
      fields.isSetField(MDEntryPx.FIELD) must beFalse
      fields.isSetField(MDEntrySize.FIELD) must beFalse
      fields.isSetField(MDEntryID.FIELD) must beFalse
      fields.isSetField(QuoteEntryID.FIELD) must beFalse
    }

    "be able to create a full FieldMap" in {
      val entry: Entry = Entry(1, myDate)
      entry.price = new BigDecimal("1.23")
      entry.size = new BigDecimal("10")
      entry.entryId = "bobSaget"
      entry.quoteEntryId = "jimmycarter"
      entry.currency = "USD"

      val fields = entry.toFields
      fields.getChar(MDEntryType.FIELD) must be equalTo (entry.entryType)
      fields.getUtcDateOnly(MDEntryDate.FIELD).getTime must be equalTo (dateOnly)
      fields.getUtcTimeOnly(MDEntryTime.FIELD).getTime must be equalTo (timeOnly)
      fields.getDecimal(MDEntryPx.FIELD) must be equalTo (entry.price.get)
      fields.getDecimal(MDEntryPx.FIELD).toString must be equalTo ("1.23")
      fields.getDecimal(MDEntrySize.FIELD) must be equalTo (entry.size.get)
      fields.getDecimal(MDEntrySize.FIELD).toString must be equalTo ("10")
      fields.getString(MDEntryID.FIELD) must be equalTo (entry.entryId.get)
      fields.getString(QuoteEntryID.FIELD) must be equalTo (entry.quoteEntryId.get)
      fields.getString(Currency.FIELD) must be equalTo (entry.currency.get)
    }

    "be created from a minimal FieldMap" in {
      val fields = Entry.newGroup[Fix50]
      fields.setChar(MDEntryType.FIELD, 2.toChar)

      val entry = Entry(fields)
      entry.entryType must be equalTo (2.toChar)
      entry.timestamp must notBeNull
      entry.entryId must be equalTo (None)
      entry.size must be equalTo (None)
      entry.price must be equalTo (None)
      entry.quoteEntryId must be equalTo (None)
      entry.currency must be equalTo (None)
    }

    "be created from a full FieldMap" in {
      val fields = Entry.newGroup[Fix50]
      fields.setChar(MDEntryType.FIELD, 2.toChar)
      fields.setUtcDateOnly(MDEntryDate.FIELD, myDate)
      fields.setUtcTimeOnly(MDEntryTime.FIELD, myDate, true)
      fields.setDecimal(MDEntryPx.FIELD, new BigDecimal("1.42"))
      fields.setDecimal(MDEntrySize.FIELD, new BigDecimal("100"))
      fields.setString(MDEntryID.FIELD, "bobSaget")
      fields.setString(QuoteEntryID.FIELD, "jimmycarter")
      fields.setString(Currency.FIELD, "USD")

      val entry = Entry(fields)
      entry.entryType must be equalTo (2.toChar)
      entry.timestamp must be equalTo (myDate)
      entry.entryId.get must be equalTo ("bobSaget")
      entry.size.get.toString must be equalTo ("100")
      entry.price.get.toString must be equalTo ("1.42")
      entry.quoteEntryId.get must be equalTo ("jimmycarter")
      entry.currency.get must be equalTo ("USD")
    }
  }
}
