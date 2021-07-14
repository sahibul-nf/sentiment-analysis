package sentiment.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import sentiment.entitys.UserReview;
import sentiment.utils.Cons;

import java.util.HashMap;
import java.util.Map;

public class ScoringBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;

    Map<String, Boolean> sentimentResult = new HashMap<String, Boolean>();

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        try {
            double negativeResult = tuple.getDoubleByField(Cons.TUPLE_POSITIVE_SCORE);
            double positiveResult = tuple.getDoubleByField(Cons.TUPLE_NEGATIVE_SCORE);
            String text = tuple.getStringByField(Cons.TUPLE_VAR_MSG);

            boolean score;
            UserReview userReview = new UserReview();
            if (negativeResult > positiveResult) {
                userReview.setSentimentResult(Cons.SENTIMENT_NEGATIVE);
                score = false;
                sentimentResult.put(text, score);
                System.out.println("Negatif");
            } else {
                userReview.setSentimentResult(Cons.SENTIMENT_POSITIVE);
                score = true;
                sentimentResult.put(text, score);
                System.out.println("Positif");
            }

        } catch (Exception e) {
            System.out.println("Ada error ni : " +e);
        }
    }

    @Override
    public void cleanup() {
        // print nilai atau isi dari field "word" ke console
        System.out.println("\nHasil sentiment analysis : ☺");
        for (Map.Entry<String, Boolean> e : sentimentResult.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
        System.out.println();
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
