package com.bostontechnologies.quickfixs.components

import quickfix.field._
import java.util.Date
import java.math.BigDecimal
import com.bostontechnologies.quickfixs.Fix50
import com.bostontechnologies.quickfixs.fields.MDStreamID
import org.specs.SpecificationWithJUnit
import Entry._

class IncrementalEntrySpec extends SpecificationWithJUnit {
  private val myDate = new Date(1234567890)
  private val dateOnly = 1209600000
  private val timeOnly = 24967890

  "An IncrementalEntry" should {

    val updateAction = 'b'
    val entryType = '1'


    "provide convenient set/get methods for its added fields" in {
      //NOTE: We leave specific testing of inherited Entry methods to the EntrySpec
      val entry = IncrementalEntry[Fix50](updateAction, entryType)
      entry.updateAction must_== updateAction
      entry.entryType must_== entryType
      entry.timestamp must notBeNull
      entry.hasSymbol must beFalse

      entry.symbol = "EUR/USD"
      entry.symbol must_== "EUR/USD"
    }

    "be able to create a minimal FieldMap" in {
      val entry: IncrementalEntry = IncrementalEntry[Fix50](updateAction, entryType)
      val fields = entry.toFields
      fields.getChar(MDUpdateAction.FIELD) must_== entry.updateAction
      fields.getChar(MDEntryType.FIELD) must_== entry.entryType
      fields.getUtcDateOnly(MDEntryDate.FIELD).getTime must notBeNull
      fields.getUtcTimeOnly(MDEntryTime.FIELD).getTime must notBeNull
      fields.isSetField(Symbol.FIELD) must beFalse
      fields.isSetField(MDEntryPx.FIELD) must beFalse
      fields.isSetField(MDEntrySize.FIELD) must beFalse
      fields.isSetField(MDEntryID.FIELD) must beFalse
      fields.isSetField(QuoteEntryID.FIELD) must beFalse
      fields.isSetField(Currency.FIELD) must beFalse
      fields.isSetField(MDStreamID.FIELD) must beFalse
    }

    "be able to create a FieldMap with a Date" in {
      val entry = IncrementalEntry[Fix50](updateAction, entryType, timestamp = new Date)
      val fields = entry.toFields
      fields.getChar(MDUpdateAction.FIELD) must_== entry.updateAction
      fields.getChar(MDEntryType.FIELD) must_== entry.entryType
      fields.getUtcDateOnly(MDEntryDate.FIELD).getTime must notBeNull
      fields.getUtcTimeOnly(MDEntryTime.FIELD).getTime must notBeNull
      fields.isSetField(Symbol.FIELD) must beFalse
      fields.isSetField(MDEntryPx.FIELD) must beFalse
      fields.isSetField(MDEntrySize.FIELD) must beFalse
      fields.isSetField(MDEntryID.FIELD) must beFalse
      fields.isSetField(QuoteEntryID.FIELD) must beFalse
      fields.isSetField(Currency.FIELD) must beFalse
      fields.isSetField(MDStreamID.FIELD) must beFalse
    }

    "be able to create a full FieldMap" in {
      val entry: IncrementalEntry = IncrementalEntry[Fix50](updateAction, entryType, timestamp = myDate)
      entry.symbol = "EUR/USD"
      entry.price = new BigDecimal("1.23")
      entry.size = new BigDecimal("10")
      entry.entryId = "bobSaget"
      entry.quoteEntryId = "jimmycarter"
      entry.currency = "USD"

      val fields = entry.toFields
      fields.getChar(MDUpdateAction.FIELD) must_== entry.updateAction
      fields.getChar(MDEntryType.FIELD) must_== entry.entryType
      fields.getUtcDateOnly(MDEntryDate.FIELD).getTime must_== dateOnly
      fields.getUtcTimeOnly(MDEntryTime.FIELD).getTime must_== timeOnly
      fields.getString(Symbol.FIELD) must_== entry.symbol
      fields.getDecimal(MDEntryPx.FIELD) must_== entry.price.get
      fields.getDecimal(MDEntrySize.FIELD) must_== entry.size.get
      fields.getString(MDEntryID.FIELD) must_== entry.entryId.get
      fields.getString(Currency.FIELD) must_== entry.currency.get
      fields.getString(QuoteEntryID.FIELD) must_== entry.quoteEntryId.get
    }

    "be created from a minimal FieldMap" in {
      val fields = IncrementalEntry.newGroup[Fix50]
      fields.setChar(MDUpdateAction.FIELD, updateAction)
      fields.setChar(MDEntryType.FIELD, entryType)

      val entry = new IncrementalEntry(fields)
      entry.updateAction must_== updateAction
      entry.entryType must_== entryType
      entry.timestamp must notBeNull
      entry.hasSymbol must beFalse
      entry.entryId must_== None
      entry.size must_== None
      entry.price must_== None
      entry.quoteEntryId must_== None
      entry.currency must_== None
    }

    "be created from a full FieldMap" in {
      val fields = IncrementalEntry.newGroup[Fix50]
      fields.setChar(MDUpdateAction.FIELD, updateAction)
      fields.setChar(MDEntryType.FIELD, entryType)
      fields.setUtcDateOnly(MDEntryDate.FIELD, myDate)
      fields.setUtcTimeOnly(MDEntryTime.FIELD, myDate, true)
      fields.setString(Symbol.FIELD, "EUR/USD")
      fields.setDecimal(MDEntryPx.FIELD, new BigDecimal("1.42"))
      fields.setDecimal(MDEntrySize.FIELD, new BigDecimal("100"))
      fields.setString(MDEntryID.FIELD, "bobSaget")
      fields.setString(QuoteEntryID.FIELD, "jimmycarter")
      fields.setString(Currency.FIELD, "USD")

      val entry = new IncrementalEntry(fields)
      entry.updateAction must_== updateAction
      entry.entryType must_== entryType
      entry.timestamp must_== myDate
      entry.symbol must_== "EUR/USD"
      entry.entryId.get must_== ("bobSaget")
      entry.size.get.toString must_== ("100")
      entry.price.get.toString must_== ("1.42")
      entry.quoteEntryId.get.toString must_== ("jimmycarter")
      entry.currency.get must_== "USD"
    }
  }
}