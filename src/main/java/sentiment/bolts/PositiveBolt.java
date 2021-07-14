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

public class PositiveBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String modifiedText = tuple.getStringByField(Cons.TUPLE_VAR_MSG);

        String[] words = modifiedText.split(" ");

        int wordsSize = words.length;
        int positiveWordsSize = 0;

        for (String word : words) {
            if (PositiveWords.get().contains(word)) {
                positiveWordsSize++;
            }
        }

        double positiveResult = (double) positiveWordsSize / wordsSize;

        UserReview userReview = new UserReview();
        userReview.setPositiveScore(positiveResult);

        basicOutputCollector.emit(new Values(positiveResult));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Cons.TUPLE_VAR_MSG));
    }

    private static class PositiveWords {
        private Set<String> positiveWords;
        private static PositiveWords _singleton;

        private PositiveWords() {
            try {
                String fileName = "src/main/resources/positive-words.txt";
                BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                String line;
                positiveWords = new HashSet<>();

                while ((line = fileReader.readLine()) != null) {
                    positiveWords.add(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan " + e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        static PositiveWords get() {
            if (_singleton == null) {
                synchronized (PositiveWords.class) {
                    if (_singleton == null) {
                        _singleton = new PositiveWords();
                    }
                }
            }

            return _singleton;
        }

        boolean contains(String key) {
            return get().positiveWords.contains(key);
        }
    }
}
