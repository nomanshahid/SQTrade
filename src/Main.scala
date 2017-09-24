import scala.io.Source
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

// TODO: change strategy to use Market to Book ratio to identify arbitrage opportunities
//TODO: Use yahoo API again once YQL is working correctly


// All rate data as of August 28, 2017 for US markets
object Main {
  val ustbill3mo = 1.00 //risk free rate (temporary)
  val sp5003mo = 2.50 //expected 3mo return on the market

  def main(args: Array[String]) {
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

    // Split, train and test data from csv file
    val spark = SparkSession
     .builder()
     .appName("SQTrade")
     .config("spark.master", "local")
     .getOrCreate()

   // Load the data stored in LIBSVM format as a DataFrame.
   val data = spark.read.format("libsvm")
     .load("data/AMZN.csv")


    // Split the data into train and test
    val splits = data.randomSplit(Array(0.6, 0.4), seed = 1234L)
    val train = splits(0)
    val test = splits(1)

    // specify layers for the neural network:
    // input layer of size 4 (features), two intermediate of size 5 and 4
    // and output of size 3 (classes)
    val layers = Array[Int](4, 5, 4, 3)

    // create the trainer and set its parameters
    val trainer = new MultilayerPerceptronClassifier()
      .setLayers(layers)
      .setBlockSize(128)
      .setSeed(1234L)
      .setMaxIter(100)

    // train the model
    val model = trainer.fit(train)

    // compute accuracy on the test set
    val result = model.transform(test)
    val predictionAndLabels = result.select("prediction", "label")
    val evaluator = new MulticlassClassificationEvaluator()
      .setMetricName("accuracy")

    println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels))
  }

  // Capital Asset Pricing Model formula
  def capm(rf:Double,b:Double,rm:Double):Double = rf+(b*(rm-rf))
}
