import org.grouplens.lenskit.knn.item.*
import org.grouplens.lenskit.baseline.*
import org.grouplens.lenskit.transform.normalize.*

target("evaluate") {
    
    trainTest("item-item algorithm") {
        algorithm("ItemItem") {
            bind ItemScorer to TFIDFItemScorer
        }

        metric CoveragePredictMetric
        metric RMSEPredictMetric
        metric NDCGPredictMetric

        output "eval-results.csv"
    }
}

defaultTarget "evaluate"