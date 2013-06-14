# QuickFixS

We use this to wrap the quickfix/j api to give us some nice scala syntax. You can find quickfixj here: http://www.quickfixj.org/

## Examples

New Order Single

```scala
val richOrder = RichNewOrderSingle.newMessage
richOrder.clOrdId = "newClOrderId"
richOrder.side = Side.BUY
richOrder.symbol = "EUR/USD"
richOrder.product = Product.CURRENCY
richOrder.orderQty = BigDecimal(100000).bigDecimal
richOrder.orderType = OrdType.PREVIOUSLY_QUOTED
richOrder.price = BigDecimal(100000).bigDecimal
richOrder.quoteId = "myQuoteId"
richOrder.transactTime = new Date()
richOrder.currency = "USD"
```

Market Data

```scala
import IncrementalEntry._
implicit val fixVersion = FixVersion.Fix50

val msg = RichMarketDataIncrementalRefresh.newMessage
msg.hasRequestId must_== false
msg.requestId = "reqId"
msg += IncrementalEntry[Fix50](MDUpdateAction.NEW, MDEntryType.BID)
msg ++= List(IncrementalEntry[Fix50](MDUpdateAction.NEW, MDEntryType.OFFER), IncrementalEntry[Fix50](MDUpdateAction.CHANGE, MDEntryType.BID))
```