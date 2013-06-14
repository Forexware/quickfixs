package com.bostontechnologies.quickfixs.messages

import quickfix.fix50.MarketDataRequest
import quickfix.FieldNotFound
import quickfix.field.{DeliverToCompID, OnBehalfOfCompID, MsgType}
import org.specs.SpecificationWithJUnit

class RichMessageSpec extends SpecificationWithJUnit {

  private val onBehalfOf = "bobSaget"
  private val deliverTo = "kilroy"

  "A RichMessage" should {

    "be able to identify a message type" in {
      val rawMsg = new MarketDataRequest
      val richMsg = RichMessage(rawMsg)

      RichMessage.messageType(rawMsg) must be equalTo (MsgType.MARKET_DATA_REQUEST)
      richMsg.messageType must be equalTo (MsgType.MARKET_DATA_REQUEST)

      RichMessage.isA(rawMsg, MsgType.MARKET_DATA_REQUEST) must beTrue
      RichMessage.isA(rawMsg, MsgType.MARKET_DATA_INCREMENTAL_REFRESH) must beFalse
      richMsg.isA(MsgType.MARKET_DATA_REQUEST) must beTrue
      richMsg.isA(MsgType.ADVERTISEMENT) must beFalse
    }

    "provide a way to create empty messages" in {
      val message = RichMessage.newMessage()
      message.messageType must throwA[FieldNotFound]
    }

    "provide a way to create a message of specific type" in {
      val message = RichMessage.newMessage(MsgType.ORDER_SINGLE)
      message.messageType must be equalTo (MsgType.ORDER_SINGLE)
      message.isA(MsgType.ORDER_SINGLE) must beTrue
    }

    "be able to handle onBehalfOf header" in {
      val message = RichMessage.newMessage()
      message.hasOnBehalfOf must beFalse
      message.self.getHeader.isSetField(OnBehalfOfCompID.FIELD) must beFalse
      message.onBehalfOf must throwA[FieldNotFound]

      message.self.getHeader.setString(OnBehalfOfCompID.FIELD, onBehalfOf)
      message.hasOnBehalfOf must beTrue
      message.onBehalfOf must be equalTo (onBehalfOf)

      message.self.clear()

      message.onBehalfOf = onBehalfOf
      message.hasOnBehalfOf must beTrue
      message.onBehalfOf must be equalTo (onBehalfOf)
      message.self.getHeader.getString(OnBehalfOfCompID.FIELD) must be equalTo (onBehalfOf)
    }

    "be able to handle deliverTo header" in {
      val message = RichMessage.newMessage()
      message.hasDeliverTo must beFalse
      message.self.getHeader.isSetField(DeliverToCompID.FIELD) must beFalse
      message.deliverTo must throwA[FieldNotFound]

      message.self.getHeader.setString(DeliverToCompID.FIELD, deliverTo)
      message.hasDeliverTo must beTrue
      message.deliverTo must be equalTo (deliverTo)

      message.self.clear()

      message.deliverTo = deliverTo
      message.hasDeliverTo must beTrue
      message.deliverTo must be equalTo (deliverTo)
      message.self.getHeader.getString(DeliverToCompID.FIELD) must be equalTo (deliverTo)
    }

  }

}
