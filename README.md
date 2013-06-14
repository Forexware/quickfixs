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

richOrder.self //get the built up message
```

Market Data

```scala
import IncrementalEntry._
implicit val fixVersion = FixVersion.Fix50 //Use FIX 5.0

val msg = RichMarketDataIncrementalRefresh.newMessage
msg.requestId = "reqId"
msg += IncrementalEntry[Fix50](MDUpdateAction.NEW, MDEntryType.BID) //Use type tag for fix versions
msg ++= List(IncrementalEntry[Fix50](MDUpdateAction.NEW, MDEntryType.OFFER), IncrementalEntry[Fix50](MDUpdateAction.CHANGE, MDEntryType.BID))
```