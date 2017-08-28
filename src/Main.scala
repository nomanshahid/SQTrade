import java.io.PrintStream
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val topGainerData = Source.fromFile("data/stock.csv")
    for(line <- topGainerData.getLines()) {
      val cols = line.split(',').map(_.trim) // trim all leading/trailing whitespace
      println(cols(0))
    }
    topGainerData.close()
  }
}
