import java.io.PrintStream
import scala.io.Source

// TODO: change strategy to use Market to Book ratio to identify arbitrage opportunities
//TODO: Use yahoo API again once YQL is working correctly

object Main {
  val ustbill3mo = 1.00 //risk free rate (temporary)
  val sp5003mo = 2.50 //expected 3mo return on the market

  def main(args: Array[String]): Unit = {
    // Screening part, apply CAPM to filter stock plays
    val topGainerData = Source.fromFile("data/stock.csv")
    for(line <- topGainerData.getLines().drop(1)) {
      val cols = line.split(',').map(_.trim) // trim all leading/trailing whitespace
      // Compare analyst estimate to required return
      val stockTicker = cols(0)
      val beta = cols(1).toDouble
      val analystEst = cols(2).toDouble
      val reqReturn = capm(ustbill3mo, beta, sp5003mo)
      if (analystEst >= reqReturn) {
        println("Arbitrage opportunity for " + stockTicker)
        println("Expected return of %"  + analystEst + " is greater than required return of %" + reqReturn)
      }
    }
    topGainerData.close()
  }

  // Capital Asset Pricing Model formula
  def capm(rf:Double,b:Double,rm:Double):Double = rf+(b*(rm-rf))
}
