package sentiment.bolts;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import sentiment.entitys.UserReview;
import sentiment.utils.Cons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FilteringBolt extends BaseBasicBolt {

    private static final long serialVersionUID = 1L;
    private transient ObjectMapper mapper;

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        try {
            String text = tuple.getStringByField(Cons.TUPLE_VAR_MSG);

            String[] words = text.split("\\b");
            StringBuilder modifiedText = new StringBuilder();

            for (String word : words) {
                if (!UselessWords.get().contains(word)) {
                    modifiedText.append(word);
                }
            }

            basicOutputCollector.emit(new Values(modifiedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Cons.TUPLE_VAR_MSG));
    }

    private static class UselessWords {
        private Set<String> uselessWords;
        private static UselessWords _singleton;

        private UselessWords() {
            try {
                String fileName = "src/main/resources/useless-words.txt";
                BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                String line;
                uselessWords = new HashSet<>();

                while ((line = fileReader.readLine()) != null) {
                    uselessWords.add(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan " + e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        static UselessWords get() {
            if (_singleton == null) {
                synchronized (UselessWords.class) {
                    if (_singleton == null) {
                        _singleton = new UselessWords();
                    }
                }
            }

            return _singleton;
        }

        boolean contains(String key) {
            return get().uselessWords.contains(key);
        }
    }
}
