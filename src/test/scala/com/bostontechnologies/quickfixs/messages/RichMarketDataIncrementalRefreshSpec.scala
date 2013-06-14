package com.bostontechnologies.quickfixs.messages

import org.specs.SpecificationWithJUnit
import com.bostontechnologies.quickfixs.components.IncrementalEntry
import quickfix.field._
import com.bostontechnologies.quickfixs.{Fix50, FixVersion}

class RichMarketDataIncrementalRefreshSpec extends SpecificationWithJUnit {
  "A RichMarketDataIncrementalRefresh" should {
    "initialize from fix50 MarketDataIncrementalRefresh" in {
      val msg = new quickfix.fix50.MarketDataIncrementalRefresh
      RichMarketDataIncrementalRefresh(msg).messageType must_==
        MsgType.MARKET_DATA_INCREMENTAL_REFRESH
    }

    "initialize from fix42 MarketDataIncrementalRefresh" in {
      val msg = new quickfix.fix42.MarketDataIncrementalRefresh
      RichMarketDataIncrementalRefresh(msg).messageType must_==
        MsgType.MARKET_DATA_INCREMENTAL_REFRESH
    }

    "initialize new message" in {
      RichMarketDataIncrementalRefresh.newMessage.messageType must_==
        MsgType.MARKET_DATA_INCREMENTAL_REFRESH
    }

    "provide convenient methods for setting, getting and checking existence of each property" in {
      val msg = RichMarketDataIncrementalRefresh.newMessage
      msg.hasRequestId must_== false
      msg.incrementalEntryCount must throwA[quickfix.FieldNotFound]

      msg.requestId = "reqId"
      import IncrementalEntry._
      implicit val fixVersion = FixVersion.Fix50
      msg += IncrementalEntry[Fix50](MDUpdateAction.NEW, MDEntryType.BID)
      msg ++= List(IncrementalEntry[Fix50](MDUpdateAction.NEW, MDEntryType.OFFER),
        IncrementalEntry[Fix50](MDUpdateAction.CHANGE, MDEntryType.BID))

      msg.hasRequestId must_== true
      msg.requestId must_== "reqId"

      msg.incrementalEntryCount must_== 3
      msg.incrementalEntries.size must_== 3
    }

  }
}