package sentiment.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import sentiment.entitys.UserReview;
import sentiment.utils.Cons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NegativeBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String modifiedText = tuple.getStringByField(Cons.TUPLE_VAR_MSG);

        String[] words = modifiedText.split(" ");

        int wordsSize = words.length;
        int negativeWordsSize = 0;

        for (String word : words) {
            if (NegativeBolt.NegativeWords.get().contains(word)) {
                negativeWordsSize++;
            }
        }

        double negativeResult = (double) negativeWordsSize / wordsSize;

        UserReview userReview = new UserReview();
        userReview.setNegativeScore(negativeResult);

        basicOutputCollector.emit(new Values(negativeResult));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Cons.TUPLE_VAR_MSG));
    }

    private static class NegativeWords {
        private Set<String> negativeWords;
        private static NegativeBolt.NegativeWords _singleton;

        private NegativeWords() {
            try {
                String fileName = "src/main/resources/negative-words.txt";
                BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                String line;
                negativeWords = new HashSet<>();

                while ((line = fileReader.readLine()) != null) {
                    negativeWords.add(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan " + e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        static NegativeBolt.NegativeWords get() {
            if (_singleton == null) {
                synchronized (NegativeBolt.NegativeWords.class) {
                    if (_singleton == null) {
                        _singleton = new NegativeBolt.NegativeWords();
                    }
                }
            }

            return _singleton;
        }

        boolean contains(String key) {
            return get().negativeWords.contains(key);
        }
    }
}
